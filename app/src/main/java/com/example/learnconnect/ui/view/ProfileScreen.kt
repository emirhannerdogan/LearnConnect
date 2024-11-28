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
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
fun ProfileScreen(
    email: String,
    registeredCourses: List<Course>,
    favoriteCourses: List<Course>,
    onNavigate: (String) -> Unit,
    preferencesHelper: PreferencesHelper,
    initialDarkMode: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    currentScreen: String
) {
    val backgroundColor = if (initialDarkMode) Color.Black else Color.White
    val textColor = if (initialDarkMode) Color.White else Color.Black

    // Açılır/Kapanır durumları
    var isRegisteredCoursesExpanded by remember { mutableStateOf(false) }
    var isFavoriteCoursesExpanded by remember { mutableStateOf(false) }
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
            // Kayıtlı Kurslar Başlığı
            HeaderCard(
                title = "Kayıtlı Kurslar",
                isExpanded = !isRegisteredCoursesExpanded,
                onClick = { isRegisteredCoursesExpanded = !isRegisteredCoursesExpanded },
                textColor = textColor,
                backgroundColor = backgroundColor
            )

            // Kayıtlı Kurslar Listesi
            if (!isRegisteredCoursesExpanded) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(registeredCourses) { course ->
                        CourseCard(course = course, textColor = textColor, onNavigate = onNavigate)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Favori Kurslar Başlığı
            HeaderCard(
                title = "Favori Kurslar",
                isExpanded = isFavoriteCoursesExpanded,
                onClick = { isFavoriteCoursesExpanded = !isFavoriteCoursesExpanded },
                textColor = textColor,
                backgroundColor = backgroundColor
            )

            // Favori Kurslar Listesi
            if (isFavoriteCoursesExpanded) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(favoriteCourses) { course ->
                        CourseCard(course = course, textColor = textColor, onNavigate = onNavigate)
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderCard(
    title: String,
    isExpanded: Boolean,
    onClick: () -> Unit,
    textColor: Color,
    backgroundColor: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        backgroundColor = if (isExpanded) backgroundColor else backgroundColor.copy(alpha = 0.9f),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = textColor
            )
        }
    }
}

// Kurs için kart tasarımı
@Composable
fun CourseCard(
    course: Course,
    textColor: Color,
    onNavigate: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colorScheme.surface,
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigate("video_list/${course.name}") }
            .padding(vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Text(
                    text = course.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = course.description,
                    fontSize = 14.sp,
                    color = textColor.copy(alpha = 0.7f)
                )
            }
        }
    }
}




