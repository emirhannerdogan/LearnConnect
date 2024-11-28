package com.example.learnconnect.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnconnect.utils.PixabayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PixabayViewModel(private val repository: PixabayRepository) : ViewModel() {

    private val _videos = MutableStateFlow<List<String>>(emptyList())
    val videos: StateFlow<List<String>> = _videos

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    fun fetchVideos(
        query: String? = null,
        videoType: String? = null,
        category: String? = null,
        safeSearch: Boolean = true
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                println("Query sent to Pixabay: $query")
                val response = repository.getVideos(
                    query = query,
                    videoType = videoType,
                    category = category,
                    safeSearch = safeSearch
                )
                _videos.value = response.hits.mapNotNull { it.videos["medium"]?.url }
                println("Fetched videos: ${_videos.value}")
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

}
