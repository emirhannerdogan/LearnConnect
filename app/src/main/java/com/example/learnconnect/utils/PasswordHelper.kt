package com.example.learnconnect.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordHelper {
    @RequiresApi(Build.VERSION_CODES.O)
    fun hashPassword(password: String, salt: ByteArray = generateSalt()): String {
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 128)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val hash = factory.generateSecret(spec).encoded
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun verifyPassword(password: String, storedPassword: String): Boolean {
        val parts = storedPassword.split(":")
        if (parts.size != 2) return false
        val salt = Base64.getDecoder().decode(parts[0])
        val hash = hashPassword(password, salt)
        return hash == storedPassword
    }

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        return salt
    }
}
