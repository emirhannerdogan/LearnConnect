package com.example.learnconnect.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learnconnect.ui.components.BottomBar

@Composable
fun SearchScreen(onNavigate: (String) -> Unit, currentScreen: String) {
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
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Placeholder Text
            Text("Search Screen", fontSize = 24.sp)

        }
    }
}
