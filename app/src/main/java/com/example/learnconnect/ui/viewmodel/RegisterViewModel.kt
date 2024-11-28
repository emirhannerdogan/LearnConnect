package com.example.learnconnect.ui.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnconnect.data.AppDatabase
import com.example.learnconnect.data.model.User
import com.example.learnconnect.utils.PasswordHelper
import com.example.learnconnect.utils.PreferencesHelper
import kotlinx.coroutines.launch
import java.util.UUID

class RegisterViewModel(application: Application, private val preferencesHelper: PreferencesHelper)
    : AndroidViewModel(application) {

    private val userDao = AppDatabase.getInstance(application).userDao()

    @RequiresApi(Build.VERSION_CODES.O)
    fun registerUser(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser == null) {
                val hashedPassword = PasswordHelper.hashPassword(password)
                userDao.insertUser(User(email = email, password = hashedPassword))
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loginUser(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = userDao.getUserByEmail(email) // Email ile kullanıcı getir
            if (user != null) {
                println("Stored Hashed Password: ${user.password}") // Veritabanındaki hashlenmiş şifre
                val isPasswordCorrect = PasswordHelper.verifyPassword(password, user.password)
                if (isPasswordCorrect) {
                    println("Password match successful!")
                    val hashedPassword = PasswordHelper.hashPassword(password)
                    preferencesHelper.saveUser(email, hashedPassword) // Hashlenmiş şifreyi kaydet
                }
                onResult(isPasswordCorrect) // Şifre doğruysa true, değilse false
            } else {
                onResult(false) // Kullanıcı bulunamadı
            }
        }
    }

}
