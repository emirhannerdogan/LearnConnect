package com.example.learnconnect.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.learnconnect.data.model.Course
import com.example.learnconnect.ui.components.BottomBar
import com.example.learnconnect.ui.viewmodel.HomeViewModel
import com.example.learnconnect.utils.PreferencesHelper

@Composable
fun HomeScreenWrapper(
    currentScreen: String,
    email: String,
    onNavigate: (String) -> Unit,
    isDarkMode: Boolean,

    preferencesHelper: PreferencesHelper,
    homeViewModel: HomeViewModel
) {
    val courses by homeViewModel.courses.collectAsState()
    val userEmail by remember {
        derivedStateOf {
            preferencesHelper.getUser()?.first.orEmpty()
        }
    }


    HomeScreen(
        currentScreen = currentScreen,
        email = userEmail,
        onNavigate = onNavigate,
        isDarkMode = isDarkMode,
        courses = courses
    )
}
@Composable
fun HomeScreen(
    currentScreen: String,
    email: String,
    isDarkMode: Boolean,
    onNavigate: (String) -> Unit,
    courses: List<Course> // Veritabanından gelen kurs listesi
) {

    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black
    Scaffold(
        bottomBar = {
            BottomBar(
                currentScreen = currentScreen,
                onItemSelected = { selectedScreen ->
                    onNavigate(selectedScreen)
                }
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
            Text(
                text = "Welcome, $email",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
                color = textColor
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(courses) { course ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = Color.White,
                        elevation = 4.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigate("video_list/${course.name}") }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Assessment, // Varsayılan bir ikon
                                contentDescription = null,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = course.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
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

