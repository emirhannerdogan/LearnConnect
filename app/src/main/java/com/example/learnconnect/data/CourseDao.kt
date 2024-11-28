package com.example.learnconnect.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnconnect.data.model.Course
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<Course>)

    @Query("SELECT * FROM courses")
    suspend fun getAllCourses(): List<Course>

    @Query("SELECT * FROM courses WHERE name = :courseName LIMIT 1")
    suspend fun getCourseByName(courseName: String): Course?

    @Query("SELECT * FROM courses WHERE id = :courseId LIMIT 1")
    suspend fun getCourseById(courseId: Int): Course?

    @Query("SELECT * FROM courses")
    fun getAllCoursesFlow(): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE category LIKE :category")
    suspend fun getCoursesByCategory(category: String): List<Course>

    @Query("SELECT * FROM courses WHERE keywords LIKE '%' || :keyword || '%'")
    suspend fun getCoursesByKeyword(keyword: String): List<Course>

    @Query("""
        SELECT * FROM courses 
        WHERE category LIKE :category 
        OR keywords LIKE '%' || :keyword || '%'
    """)
    suspend fun searchCourses(category: String, keyword: String): List<Course>

    @Query("SELECT DISTINCT category FROM courses")
    suspend fun getAllCategories(): List<String>

}
