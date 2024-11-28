package com.example.learnconnect.utils

import com.example.learnconnect.api.PixabayApi
import com.example.learnconnect.api.RetrofitInstance

class PixabayRepository {

    private val api: PixabayApi = RetrofitInstance.api

    suspend fun getVideos(
        query: String? = null,
        videoType: String? = null,
        category: String? = null,
        minWidth: Int? = null,
        minHeight: Int? = null,
        editorsChoice: Boolean? = null,
        safeSearch: Boolean? = null,
        order: String? = null,
        perPage: Int = 10,
        page: Int = 1
    ) = try {
        val response = api.getVideos(
            apiKey = "29290287-4eea382f80dc0d6412552d3ae",
            query = query,
            videoType = videoType,
            category = category,
            minWidth = minWidth,
            minHeight = minHeight,
            editorsChoice = editorsChoice,
            safeSearch = safeSearch,
            order = order,
            perPage = perPage,
            page = page
        )

        println("Pixabay API Response: $response") // Yanıtı logla
        response
    } catch (e: Exception) {
        println("Error fetching videos: ${e.message}") // Hata durumunu logla
        throw e // Hata fırlatarak üst katmanların bu durumu bilmesini sağla
    }
}

