package com.example.learnconnect.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnconnect.ui.components.BottomBar
import com.example.learnconnect.utils.PreferencesHelper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.learnconnect.ui.viewmodel.ProfileViewModel
import com.example.learnconnect.data.model.Course

@Composable
fun ProfileScreenWrapper(
    email: String,
    onNavigate: (String) -> Unit,
    preferencesHelper: PreferencesHelper,
    profileViewModel: ProfileViewModel,
    currentScreen: String,
    isDarkMode: Boolean,
    onToggleTheme: (Boolean) -> Unit
) {
    val userEmail by remember {
        derivedStateOf {
            preferencesHelper.getUser()?.first.orEmpty()
        }
    }

    val registeredCourses by profileViewModel.registeredCourses.collectAsState()

    // Kullanıcıya ait kayıtlı kursları çek
    LaunchedEffect(userEmail) {
        if (userEmail.isNotEmpty()) {
            profileViewModel.fetchRegisteredCourses(userEmail)
        }
    }

    ProfileScreen(
        email = userEmail,
        onNavigate = onNavigate,
        preferencesHelper = preferencesHelper,
        currentScreen = currentScreen,
        onToggleTheme = onToggleTheme,
        registeredCourses = registeredCourses,
        initialDarkMode = isDarkMode
    )
}


@Composable
fun ProfileScreen(
    email: String,
    registeredCourses: List<Course>, // Kayıtlı kurslar
    onNavigate: (String) -> Unit,
    preferencesHelper: PreferencesHelper,
    initialDarkMode: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    currentScreen: String
) {
    val backgroundColor = if (initialDarkMode) Color.Black else Color.White
    val textColor = if (initialDarkMode) Color.White else Color.Black

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = email, // E-posta göster
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // Light/Dark Mode Switch
                    IconButton(
                        onClick = {
                            // Tema değişimini tetikle
                            onToggleTheme(!initialDarkMode)
                        }
                    ) {
                        Icon(
                            imageVector = if (initialDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (initialDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                            tint = textColor
                        )
                    }

                    // Logout IconButton
                    IconButton(
                        onClick = { onNavigate("logout") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = textColor
                        )
                    }
                },
                backgroundColor = backgroundColor
            )
        },
        bottomBar = {
            BottomBar(
                currentScreen = currentScreen,
                onItemSelected = { selectedScreen -> onNavigate(selectedScreen) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Başlık
            Text(
                text = "Kayıtlı Kurslar",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // LazyColumn ile kursları listele
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(registeredCourses) { course ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = if (initialDarkMode) Color.DarkGray else Color.White,
                        elevation = 4.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigate("video_list/${course.name}") }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Column {
                                Text(
                                    text = course.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textColor
                                )
                                Text(
                                    text = course.description,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}




