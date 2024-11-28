package com.example.learnconnect.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.CompositionLocalProvider

// Varsayılan olarak Light Mode
val LocalThemeState = compositionLocalOf { false }

// Tema sağlayıcı
@Composable
fun ThemeProvider(
    isDarkMode: MutableState<Boolean>,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalThemeState provides isDarkMode.value) {
        content()
    }
}
