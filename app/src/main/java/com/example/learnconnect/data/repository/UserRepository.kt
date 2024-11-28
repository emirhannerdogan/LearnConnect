package com.example.learnconnect.data.repository

import com.example.learnconnect.data.AppDatabase
import com.example.learnconnect.data.model.Course
import com.example.learnconnect.data.model.UserCourse

class UserRepository(private val db: AppDatabase) {
    suspend fun getUserCourses(userId: Int): List<Course> {
        return db.userCourseDao().getCoursesForUser(userId)
    }

    suspend fun registerCourseForUser(userId: Int, courseId: Int) {
        db.userCourseDao().registerUserToCourse(UserCourse(userId, courseId))
    }
}
