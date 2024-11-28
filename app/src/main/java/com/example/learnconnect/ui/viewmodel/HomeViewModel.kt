package com.example.learnconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnconnect.data.CourseDao
import com.example.learnconnect.data.model.Course
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val courseDao: CourseDao) : ViewModel() {

    // Tüm kursları saklayan StateFlow
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    // Kategorileri saklayan StateFlow
    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    // Seçilen kategori
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    // anahtar kelimeler
    private val _searchResults = MutableStateFlow<List<Course>>(emptyList())
    val searchResults: StateFlow<List<Course>> = _searchResults

    init {
        fetchCourses() // Tüm kursları çek
        fetchCategories() // Kategorileri çek
    }

    // Tüm kursları çek
    private fun fetchCourses() {
        viewModelScope.launch {
            _courses.value = courseDao.getAllCourses()
            println("Fetched Courses: ${_courses.value}")
        }
    }

    // Kategorileri çek
    private fun fetchCategories() {
        viewModelScope.launch {
            _categories.value = courseDao.getAllCategories()
            println("Fetched Categories: ${_categories.value}")
        }
    }

    // Seçilen kategoriye göre kursları filtrele
    fun filterCoursesByCategory(category: String?) {
        viewModelScope.launch {
            _selectedCategory.value = category
            _courses.value = if (category.isNullOrEmpty()) {
                courseDao.getAllCourses() // Kategori seçilmemişse tüm kurslar
            } else {
                courseDao.getCoursesByCategory(category) // Kategoriye göre filtreleme
            }
            println("Filtered Courses: ${_courses.value}")
        }
    }
    fun searchCoursesByKeyword(keyword: String) {
        viewModelScope.launch {
            val normalizedKeyword = normalize(keyword) // Anahtar kelimeyi normalize et
            val results = courseDao.getAllCourses().filter { course ->
                normalize(course.keywords).contains(normalizedKeyword, ignoreCase = true) ||
                        normalize(course.name).contains(normalizedKeyword, ignoreCase = true)
            }
            _searchResults.value = results
        }
    }
    fun normalize(input: String): String {
        val turkishMap = mapOf(
            'ç' to 'c', 'Ç' to 'C',
            'ğ' to 'g', 'Ğ' to 'G',
            'ı' to 'i', 'I' to 'I',
            'ö' to 'o', 'Ö' to 'O',
            'ş' to 's', 'Ş' to 'S',
            'ü' to 'u', 'Ü' to 'U'
        )
        return input.map { char -> turkishMap[char] ?: char }.joinToString("")
    }



}
