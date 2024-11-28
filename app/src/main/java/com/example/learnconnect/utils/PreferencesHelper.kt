package com.example.learnconnect.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("LearnConnectPrefs", Context.MODE_PRIVATE)

    fun saveUser(email: String, password: String) {
        sharedPreferences.edit().apply {
            putString("user_email", email)
            putString("user_password", password)
            apply()
        }
    }

    fun getUser(): Pair<String, String>? {
        val email = sharedPreferences.getString("user_email", null)
        val password = sharedPreferences.getString("user_password", null)
        return if (email != null && password != null) {
            Pair(email, password)
        } else {
            null
        }
    }

    fun clearUser() {
        sharedPreferences.edit().clear().apply()
    }

    // Token Kaydetme
    fun saveToken(token: String) {
        sharedPreferences.edit().apply {
            putString("user_token", token)
            apply()
        }
    }

    // Token Okuma
    fun getToken(): String? {
        return sharedPreferences.getString("user_token", null)
    }

    // Token Silme
    fun clearToken() {
        sharedPreferences.edit().remove("user_token").apply()
    }

    // PreferencesHelper.kt (zaten utils klasöründe)
    fun saveTheme(isDarkMode: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean("is_dark_mode", isDarkMode)
            apply()
        }
    }

    fun getTheme(): Boolean {
        return sharedPreferences.getBoolean("is_dark_mode", false) // Varsayılan olarak Light Mode
    }

    fun saveDarkModePreference(isDarkMode: Boolean) {
        sharedPreferences.edit().putBoolean("dark_mode", isDarkMode).apply()
    }

    fun getDarkModePreference(): Boolean {
        return sharedPreferences.getBoolean("dark_mode", false) // Varsayılan olarak light mode
    }


}

