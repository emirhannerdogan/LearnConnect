package com.example.learnconnect.ui.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import androidx.compose.runtime.remember
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.android.exoplayer2.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView
import com.example.learnconnect.data.AppDatabase
import com.example.learnconnect.data.model.VideoProgressEntity
import com.example.learnconnect.ui.theme.LocalThemeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun VideoPlayerScreen(
    videoUrl: String,
    initialProgress: Int,
    courseId: Int,
    videoIndex: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val videoProgressDao = AppDatabase.getInstance(context).videoProgressDao()

    val isDarkMode = LocalThemeState.current
    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black

    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
            seekTo(initialProgress.toLong() * 1000)
            prepare()
            playWhenReady = true
        }
    }

    var playbackSpeed by remember { mutableFloatStateOf(1.0f) } // Varsayılan hız

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release() // ExoPlayer kaynaklarını serbest bırak
        }
    }

    LaunchedEffect(exoPlayer) {
        while (true) {
            val progress = (exoPlayer.currentPosition / 1000).toLong()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    videoProgressDao.insertOrUpdateProgress(
                        VideoProgressEntity(
                            courseId = courseId,
                            videoIndex = videoIndex,
                            progress = progress
                        )
                    )
                    println("Progress düzenli kaydedildi: courseId=$courseId, videoIndex=$videoIndex, progress=$progress")
                } catch (e: Exception) {
                    println("Progress kaydedilirken hata oluştu: ${e.message}")
                }
            }
            kotlinx.coroutines.delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onBack, modifier = Modifier.align(Alignment.Start)) {
            Text("Back")
        }

        Text(
            text = "Playing Video",
            fontSize = 24.sp,
            modifier = Modifier.padding(vertical = 16.dp),
            color = textColor
        )

        // Video Player
        AndroidView(factory = {
            PlayerView(context).apply {
                player = exoPlayer
            }
        }, modifier = Modifier.fillMaxWidth().height(300.dp))

        // Hız Kontrol Butonları
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                playbackSpeed = 0.5f
                exoPlayer.setPlaybackParameters(exoPlayer.playbackParameters.withSpeed(playbackSpeed))
            }) {
                Text("0.5x")
            }
            Button(onClick = {
                playbackSpeed = 1.0f
                exoPlayer.setPlaybackParameters(exoPlayer.playbackParameters.withSpeed(playbackSpeed))
            }) {
                Text("1x")
            }
            Button(onClick = {
                playbackSpeed = 1.5f
                exoPlayer.setPlaybackParameters(exoPlayer.playbackParameters.withSpeed(playbackSpeed))
            }) {
                Text("1.5x")
            }
            Button(onClick = {
                playbackSpeed = 2.0f
                exoPlayer.setPlaybackParameters(exoPlayer.playbackParameters.withSpeed(playbackSpeed))
            }) {
                Text("2x")
            }
        }

        // Mevcut Hız Göstergesi
        Text(
            text = "Current Speed: ${playbackSpeed}x",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
