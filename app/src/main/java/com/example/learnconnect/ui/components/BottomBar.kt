package com.example.learnconnect.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BottomBar(
    currentScreen: String,
    onItemSelected: (String) -> Unit
) {
    BottomNavigation(
        backgroundColor = Color.DarkGray,
        contentColor = Color.White
    ) {
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = {
                Text(text = "Home")
            },
            selected = currentScreen == "home",
            onClick = {
                onItemSelected("home")
            },
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = Color.White
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            label = {
                Text(text = "Search")
            },
            selected = currentScreen == "search",
            onClick = {
                onItemSelected("search")
            },
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = Color.White
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Person,
                    contentDescription = "Profile"
                )
            },
            label = {
                Text(text = "Profile")
            },
            selected = currentScreen == "profile",
            onClick = {
                onItemSelected("profile")
            },
            selectedContentColor = MaterialTheme.colors.primary,
            unselectedContentColor = Color.White
        )
    }
}
