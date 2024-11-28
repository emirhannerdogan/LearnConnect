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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Composable
fun ProfileScreen(
    email: String,
    onNavigate: (String) -> Unit,
    preferencesHelper: PreferencesHelper,
    currentScreen: String,
    initialDarkMode: Boolean, // Başlangıç durumu
    onToggleTheme: (Boolean) -> Unit
) {
    // isDarkMode durumunu takip eden bir `mutableStateOf` oluşturun
    var isDarkMode by remember { mutableStateOf(initialDarkMode) }

    val backgroundColor = if (isDarkMode) androidx.compose.ui.graphics.Color.Black else androidx.compose.ui.graphics.Color.White
    val textColor = if (isDarkMode) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.Black

    Scaffold(
        modifier = Modifier.background(backgroundColor), // Arka plan
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
                .background(backgroundColor)
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Profile",
                fontSize = 32.sp,
                color = textColor // Yazı rengi
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your email: $email",
                fontSize = 16.sp,
                color = textColor // Yazı rengi
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onNavigate("logout") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Logout",
                    color = textColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Tema değişimini tetikle
                    isDarkMode = !isDarkMode
                    onToggleTheme(isDarkMode)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                    color = textColor
                )
            }
        }
    }
}


