package com.example.learnconnect.api

import retrofit2.http.GET
import retrofit2.http.Query

data class PixabayResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<PixabayVideo>
)

data class PixabayVideo(
    val id: Int,
    val pageURL: String,
    val videos: Map<String, VideoDetails>
)

data class VideoDetails(
    val url: String,
    val width: Int,
    val height: Int,
    val size: Int
)

interface PixabayApi {

    @GET("videos/")
    suspend fun getVideos(
        @Query("key") apiKey: String,
        @Query("q") query: String? = null, // Arama terimi
        @Query("video_type") videoType: String? = null, // Video türü
        @Query("category") category: String? = null, // Kategori
        @Query("min_width") minWidth: Int? = null, // Minimum genişlik
        @Query("min_height") minHeight: Int? = null, // Minimum yükseklik
        @Query("editors_choice") editorsChoice: Boolean? = null, // Editör seçimi
        @Query("safesearch") safeSearch: Boolean? = null, // Güvenli arama
        @Query("order") order: String? = null, // Sıralama
        @Query("per_page") perPage: Int = 20, // Sayfa başına sonuç
        @Query("page") page: Int = 1 // Sayfa numarası
    ): PixabayResponse
}

