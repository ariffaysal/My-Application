package com.example.myapplication.util

import android.util.Patterns

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidName(name: String): Boolean {
        return name.trim().length >= 2
    }

    fun validateLoginInput(email: String, password: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Error("Email is required")
            !isValidEmail(email) -> ValidationResult.Error("Please enter a valid email")
            password.isBlank() -> ValidationResult.Error("Password is required")
            !isValidPassword(password) -> ValidationResult.Error("Password must be at least 6 characters")
            else -> ValidationResult.Success
        }
    }

    fun validateRegistrationInput(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Error("Name is required")
            !isValidName(name) -> ValidationResult.Error("Name must be at least 2 characters")
            email.isBlank() -> ValidationResult.Error("Email is required")
            !isValidEmail(email) -> ValidationResult.Error("Please enter a valid email")
            password.isBlank() -> ValidationResult.Error("Password is required")
            !isValidPassword(password) -> ValidationResult.Error("Password must be at least 6 characters")
            password != confirmPassword -> ValidationResult.Error("Passwords do not match")
            else -> ValidationResult.Success
        }
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}
