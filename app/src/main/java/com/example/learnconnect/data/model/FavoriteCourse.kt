package com.example.learnconnect.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_courses")
data class FavoriteCourse(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int, // Kullanıcı ID'si
    val courseId: Int // Favorilere eklenen kurs ID'si
)
