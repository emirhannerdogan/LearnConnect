package com.example.learnconnect.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learnconnect.utils.PreferencesHelper

class RegisterViewModelFactory(
    private val application: Application,
    private val preferencesHelper: PreferencesHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(application, preferencesHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
