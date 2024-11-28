package com.example.learnconnect.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_progress")
data class VideoProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val courseId: Int, // Kurs ID
    val videoIndex: Int, // Video'nun sırası
    val progress: Long // İlerleme süresi (milisaniye)
)
