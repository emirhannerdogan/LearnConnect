package com.example.learnconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnconnect.data.UserCourseDao
import com.example.learnconnect.data.UserDao
import com.example.learnconnect.data.model.Course
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userCourseDao: UserCourseDao,
    private val userDao: UserDao // Kullanıcı verilerini almak için
) : ViewModel() {

    private val _registeredCourses = MutableStateFlow<List<Course>>(emptyList())
    val registeredCourses: StateFlow<List<Course>> = _registeredCourses

    fun fetchRegisteredCourses(email: String) {
        viewModelScope.launch {
            val user = userDao.getUserByEmail(email)
            val userId = user?.id ?: return@launch // Kullanıcı bulunamazsa hiçbir şey yapma
            val courses = userCourseDao.getCoursesForUser(userId)
            _registeredCourses.value = courses
        }
    }
}

