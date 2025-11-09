package com.example.sqliteactappdev.util

object ValidationUtils {
    const val MIN_PASSWORD_LENGTH = 4

    fun validateUsername(username: String): ValidationResult {
        return when {
            username.isBlank() -> ValidationResult.Error("Username cannot be empty")
            username.length < 3 -> ValidationResult.Error("Username must be at least 3 characters")
            else -> ValidationResult.Success
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Error("Password cannot be empty")
            password.length < MIN_PASSWORD_LENGTH -> ValidationResult.Error("Password must be at least $MIN_PASSWORD_LENGTH characters")
            else -> ValidationResult.Success
        }
    }

    fun validatePasswordMatch(password: String, confirmPassword: String): ValidationResult {
        return when {
            password != confirmPassword -> ValidationResult.Error("Passwords do not match")
            else -> ValidationResult.Success
        }
    }

    fun validateLoginFields(username: String, password: String): ValidationResult {
        return when {
            username.isBlank() || password.isBlank() -> ValidationResult.Error("Please fill in all fields")
            else -> ValidationResult.Success
        }
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

