package com.example.learnconnect.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnconnect.data.AppDatabase
import com.example.learnconnect.data.model.FavoriteCourse
import com.example.learnconnect.ui.components.BottomBar
import com.example.learnconnect.ui.theme.LocalThemeState
import com.example.learnconnect.ui.viewmodel.PixabayViewModel
import com.example.learnconnect.utils.PreferencesHelper
import kotlinx.coroutines.launch

@Composable
fun VideoListScreen(
    courseName: String,
    courseDescription: String,
    instructorName: String,
    pixabayViewModel: PixabayViewModel,
    preferencesHelper: PreferencesHelper,
    isRegistered: Boolean,
    onRegisterCourse: () -> Unit,
    onNavigateToVideo: (String, Int) -> Unit, // `progress` bilgisi ile yönlendirme
    onBack: () -> Unit,
    currentScreen: String,
    onNavigate: (String) -> Unit
) {
    val isDarkMode = LocalThemeState.current // Tema durumunu al
    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black

    val videos by pixabayViewModel.videos.collectAsState()
    val error by pixabayViewModel.error.collectAsState()
    val loading by pixabayViewModel.loading.collectAsState()

    val videoProgress = remember { mutableStateMapOf<Int, Int>() } // [Index -> Progress]

    val context = LocalContext.current

    val videoProgressDao = AppDatabase.getInstance(context).videoProgressDao()

    val favoriteCourseDao = AppDatabase.getInstance(context).favoriteCourseDao()
    val userDao = AppDatabase.getInstance(context).userDao()
    val courseDao = AppDatabase.getInstance(context).courseDao()

    val userEmail = remember { preferencesHelper.getUser()?.first.orEmpty() }
    val userId = remember { mutableStateOf(0) }
    val isFavorited = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isRegistered, videos) {
        val user = userDao.getUserByEmail(userEmail)
        userId.value = user?.id ?: 0

        // Favori durumu kontrolü
        val course = courseDao.getCourseByName(courseName)
        if (course != null) {
            isFavorited.value = favoriteCourseDao.isCourseFavorited(userId.value, course.id)
        }
        if (isRegistered) {
            println("Pixabay'dan video aranıyor: $courseName")
            pixabayViewModel.setLoading(true)
            pixabayViewModel.fetchVideos(query = courseName)

            val courseDao = AppDatabase.getInstance(context).courseDao()
            val course = courseDao.getCourseByName(courseName)
            println("Fetched course: $course")

            val courseId = course?.id ?: -1
            println("Fetched courseId: $courseId")

            if (courseId != -1) {
                videoProgressDao.getAllProgressForCourse(courseId).collect { progressList ->
                    progressList.forEach { progressEntity ->
                        videoProgress[progressEntity.videoIndex] = progressEntity.progress.toInt()
                        println("Progress güncellendi: videoIndex=${progressEntity.videoIndex}, progress=${progressEntity.progress}")
                    }
                    pixabayViewModel.setLoading(false)
                }
            } else {
                println("Geçersiz courseId")
            }
        }
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Course: $courseName", color = textColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = textColor
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                val course = courseDao.getCourseByName(courseName)
                                course?.let {
                                    if (isFavorited.value) {
                                        favoriteCourseDao.removeCourseFromFavorites(userId.value, it.id)
                                        isFavorited.value = false
                                    } else {
                                        favoriteCourseDao.addCourseToFavorites(
                                            FavoriteCourse(
                                                userId = userId.value,
                                                courseId = it.id
                                            )
                                        )
                                        isFavorited.value = true
                                    }
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorited.value) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                            contentDescription = "Favorite",
                            tint = if (isFavorited.value) Color.Red else textColor
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomBar(
                currentScreen = currentScreen,
                onItemSelected = onNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(backgroundColor)
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = courseName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                color = textColor
            )

            Text(
                text = courseDescription,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp),
                color = textColor
            )

            Text(
                text = "Instructor: $instructorName",
                fontSize = 14.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                modifier = Modifier.padding(bottom = 16.dp),
                color = textColor
            )

            error?.let {
                Text(
                    text = "Error: $it",
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (!isRegistered) {
                Button(
                    onClick = onRegisterCourse,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Register for this Course", color = textColor)
                }
            }

            if (isRegistered) {
                Text(
                    text = "Course Videos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                    color = textColor
                )
                if (loading || videos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator() // Yüklenme sürecinde gösterilen spinner
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(videos) { index, videoUrl ->
                            val progress = videoProgress[index] ?: 0 // İlerleme durumu

                            Card(
                                elevation = 4.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onNavigateToVideo(videoUrl, progress) } // İlerlemeyi aktar
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Ders ${index + 1}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,

                                    )
                                    Text(
                                        text = "Progress: $progress seconds", // İlerleme durumunu göster
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }



            }
        }
    }
}




