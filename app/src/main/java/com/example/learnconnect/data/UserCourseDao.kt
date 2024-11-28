package com.example.learnconnect.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnconnect.data.model.Course
import com.example.learnconnect.data.model.UserCourse

@Dao
interface UserCourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registerUserToCourse(userCourse: UserCourse)

    @Query("""
        SELECT c.* 
        FROM courses c
        INNER JOIN user_courses uc ON c.id = uc.courseId
        WHERE uc.userId = :userId
    """)
    suspend fun getCoursesForUser(userId: Int): List<Course>

    @Query("""
        SELECT COUNT(*) > 0 
        FROM user_courses 
        WHERE userId = :userId AND courseId = :courseId
    """)
    suspend fun isUserRegisteredToCourse(userId: Int, courseId: Int): Boolean
}
