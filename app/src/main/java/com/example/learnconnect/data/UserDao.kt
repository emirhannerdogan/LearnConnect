package com.example.learnconnect.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.learnconnect.data.model.User

@Dao
interface UserDao {

    // Kullanıcı ekleme
    @Insert
    suspend fun insertUser(user: User): Long

    // E-posta adresine göre kullanıcı bilgilerini alma
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    // ID'ye göre kullanıcı bilgilerini alma
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): User?
}
