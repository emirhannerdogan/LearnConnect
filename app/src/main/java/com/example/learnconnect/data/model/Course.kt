package com.example.learnconnect.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String, // Kurs adı
    val description: String, // Kurs açıklaması
    val instructorName: String, // Eğitmen adı
    val category: String, // Kurs kategorisi
    val keywords: String // Anahtar kelimeler (virgülle ayrılmış)
)

