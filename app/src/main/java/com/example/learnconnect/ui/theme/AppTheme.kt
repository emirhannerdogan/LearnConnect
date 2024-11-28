package com.example.learnconnect.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    background = Color(0xFFFFFFFF), // Beyaz arka plan
    onBackground = Color(0xFF000000), // Siyah yazılar
    primary = Color(0xFF6200EE),
    onPrimary = Color.White
)

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF121212), // Siyah arka plan
    onBackground = Color(0xFFFFFFFF), // Beyaz yazılar
    primary = Color(0xFFBB86FC),
    onPrimary = Color.Black
)

@Composable
fun AppTheme(
    isDarkMode: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkMode) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
