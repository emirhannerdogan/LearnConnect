package com.example.learnconnect.data.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "user_courses",
    primaryKeys = ["userId", "courseId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Course::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserCourse(
    val userId: Int, // Kullanıcı ID
    val courseId: Int // Kurs ID
)
