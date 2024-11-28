package com.example.learnconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learnconnect.utils.PixabayRepository

class PixabayViewModelFactory(
    private val repository: PixabayRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PixabayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PixabayViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
