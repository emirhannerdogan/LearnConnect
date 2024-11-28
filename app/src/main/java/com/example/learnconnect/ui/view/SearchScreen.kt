package com.example.learnconnect.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnconnect.data.model.Course
import com.example.learnconnect.ui.components.BottomBar

@Composable
fun SearchScreen(
    currentScreen: String,
    courses: List<Course>, // Tüm kursları parametre olarak alır
    onNavigate: (String) -> Unit,
    isDarkMode: Boolean
) {
    // Arama çubuğu için state
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    // Arama sonucuna göre filtrelenmiş kurslar
    val filteredCourses = remember(searchQuery) {
        courses.filter { course ->
            course.name.contains(searchQuery, ignoreCase = true) // Kurs ismine göre filtrele
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus() // Klavyeyi açtırmak için odaklanma
    }

    // Tema renklerini belirleme
    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black

    Scaffold(
        bottomBar = {
            BottomBar(
                currentScreen = currentScreen,
                onItemSelected = onNavigate
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
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search", color = textColor) },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = textColor,
                    cursorColor = textColor,
                    focusedBorderColor = textColor,
                    unfocusedBorderColor = textColor
                ),
                singleLine = true
            )

            // LazyColumn ile filtrelenmiş kursları gösterme
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredCourses) { course ->
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
                            // Kurs bilgileri
                            Column {
                                Text(
                                    text = course.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textColor
                                )
                                Spacer(modifier = Modifier.height(4.dp))
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
