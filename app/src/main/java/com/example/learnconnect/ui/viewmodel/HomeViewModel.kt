package com.example.learnconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnconnect.data.CourseDao
import com.example.learnconnect.data.model.Course
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val courseDao: CourseDao) : ViewModel() {

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    init {
        fetchCourses()
    }

    private fun fetchCourses() {
        viewModelScope.launch {
            _courses.value = courseDao.getAllCourses()
            println("Courses: ${_courses.value}") // Burada değeri yazdırıyoruz
        }
    }

}
