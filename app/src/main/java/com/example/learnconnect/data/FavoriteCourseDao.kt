package com.example.learnconnect.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnconnect.data.model.FavoriteCourse
import com.example.learnconnect.data.model.Course

@Dao
interface FavoriteCourseDao {

    // Favorilere kurs ekle
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCourseToFavorites(favoriteCourse: FavoriteCourse)

    // Belirli bir kullanıcı için favori kursları getir
    @Query("""
        SELECT c.* FROM courses c
        INNER JOIN favorite_courses fc ON c.id = fc.courseId
        WHERE fc.userId = :userId
    """)
    suspend fun getFavoriteCoursesForUser(userId: Int): List<Course>

    // Belirli bir kursun favori olup olmadığını kontrol et
    @Query("""
        SELECT COUNT(*) > 0 FROM favorite_courses 
        WHERE userId = :userId AND courseId = :courseId
    """)
    suspend fun isCourseFavorited(userId: Int, courseId: Int): Boolean

    // Favorilerden kursu kaldır
    @Query("DELETE FROM favorite_courses WHERE userId = :userId AND courseId = :courseId")
    suspend fun removeCourseFromFavorites(userId: Int, courseId: Int)
}
