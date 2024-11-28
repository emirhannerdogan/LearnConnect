package com.example.learnconnect

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.MaterialTheme
import com.example.learnconnect.ui.navigation.NavGraph
import com.example.learnconnect.ui.theme.AppTheme
import com.example.learnconnect.ui.viewmodel.PixabayViewModel
import com.example.learnconnect.ui.viewmodel.PixabayViewModelFactory
import com.example.learnconnect.utils.PreferencesHelper
import com.example.learnconnect.ui.viewmodel.RegisterViewModelFactory
import com.example.learnconnect.ui.viewmodel.RegisterViewModel
import com.example.learnconnect.utils.PixabayRepository

class MainActivity : ComponentActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var preferencesHelper: PreferencesHelper
    private lateinit var pixabayViewModel: PixabayViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferencesHelper = PreferencesHelper(this)

        // RegisterViewModel'i başlat
        val factory = RegisterViewModelFactory(application, preferencesHelper)
        registerViewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)

        // PixabayViewModel'i başlat
        val pixabayRepository = PixabayRepository()
        val pixabayFactory = PixabayViewModelFactory(pixabayRepository)
        pixabayViewModel = ViewModelProvider(this, pixabayFactory).get(PixabayViewModel::class.java)

        val isDarkMode = preferencesHelper.getDarkModePreference()

        setContent {
            AppTheme(isDarkMode = isDarkMode){
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    registerViewModel = registerViewModel,
                    preferencesHelper = preferencesHelper,
                    pixabayViewModel = pixabayViewModel
                )
            }
        }
    }
}
