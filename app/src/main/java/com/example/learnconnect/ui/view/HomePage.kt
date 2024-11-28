package com.example.learnconnect.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
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
import androidx.compose.material.icons.filled.Search
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
    val categories by homeViewModel.categories.collectAsState(initial = emptyList())

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
        courses = courses,
        categories = categories,
        onCategorySelected = { selectedCategory ->
            homeViewModel.filterCoursesByCategory(selectedCategory)
        }

    )
}
@Composable
fun HomeScreen(
    currentScreen: String,
    email: String,
    isDarkMode: Boolean,
    onNavigate: (String) -> Unit,
    courses: List<Course>,
    categories: List<String>,
    onCategorySelected: (String?) -> Unit
) {

    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black
    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Welcome, $email",
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            // Search ekranına yönlendir
                            onNavigate("search")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search, // Varsayılan bir arama ikonu
                            contentDescription = "Search",
                            tint = textColor
                        )
                    }
                },
                backgroundColor = backgroundColor,
                elevation = 4.dp
            )
        },
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
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        backgroundColor = if (isDarkMode) Color.DarkGray else Color.LightGray,
                        elevation = 4.dp,
                        modifier = Modifier
                            .clickable { onCategorySelected(null) } // Tüm kursları göster
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "All",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = if (isDarkMode) Color.White else Color.Black
                        )
                    }
                }
                items(categories) { category ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        backgroundColor = if (isDarkMode) Color.DarkGray else Color.LightGray,
                        elevation = 4.dp,
                        modifier = Modifier
                            .clickable { onCategorySelected(category) }
                            .padding(8.dp)
                    ) {
                        Text(
                            text = category,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = if (isDarkMode) Color.White else Color.Black
                        )
                    }
                }

            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(courses) { course ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = if (isDarkMode) Color.DarkGray else Color.White,
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

