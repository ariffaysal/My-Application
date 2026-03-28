package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.UserRole
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.model.User
import kotlinx.coroutines.launch
import java.util.UUID

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getDatabase(application).userDao()

    private val _signupError = mutableStateOf<String?>(null)
    val signupError: State<String?> = _signupError

    private val _signupSuccess = mutableStateOf(false)
    val signupSuccess: State<Boolean> = _signupSuccess

    sealed class ValidationResult {
        data object Valid : ValidationResult()
        data class Invalid(val message: String) : ValidationResult()
    }

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Invalid("Email is required")
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> 
                ValidationResult.Invalid("Please enter a valid email address")
            else -> ValidationResult.Valid
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Invalid("Password is required")
            password.length < 6 -> ValidationResult.Invalid("Password must be at least 6 characters")
            else -> ValidationResult.Valid
        }
    }

    fun validateFullName(fullName: String): ValidationResult {
        return when {
            fullName.isBlank() -> ValidationResult.Invalid("Full name is required")
            fullName.length < 2 -> ValidationResult.Invalid("Full name must be at least 2 characters")
            else -> ValidationResult.Valid
        }
    }

    fun signup(
        fullName: String,
        email: String,
        password: String,
        role: UserRole,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _signupError.value = null

            val fullNameValidation = validateFullName(fullName)
            if (fullNameValidation is ValidationResult.Invalid) {
                _signupError.value = fullNameValidation.message
                return@launch
            }

            val emailValidation = validateEmail(email)
            if (emailValidation is ValidationResult.Invalid) {
                _signupError.value = emailValidation.message
                return@launch
            }

            val passwordValidation = validatePassword(password)
            if (passwordValidation is ValidationResult.Invalid) {
                _signupError.value = passwordValidation.message
                return@launch
            }

            try {
                if (userDao.emailExists(email)) {
                    _signupError.value = "An account with this email already exists"
                    return@launch
                }

                val passwordHash = hashPassword(password)
                val user = User(
                    id = UUID.randomUUID().toString(),
                    fullName = fullName,
                    email = email,
                    passwordHash = passwordHash,
                    role = role.name
                )

                userDao.insert(user)
                _signupSuccess.value = true
                onSuccess()
            } catch (e: Exception) {
                _signupError.value = "Failed to create account: ${e.message}"
            }
        }
    }

    fun login(email: String, password: String, onResult: (UserRole?) -> Unit) {
        viewModelScope.launch {
            try {
                val passwordHash = hashPassword(password)
                val user = userDao.validateCredentials(email, passwordHash)
                
                if (user != null) {
                    onResult(UserRole.valueOf(user.role))
                } else {
                    onResult(null)
                }
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }

    fun clearError() {
        _signupError.value = null
    }

    fun resetSignupSuccess() {
        _signupSuccess.value = false
    }

    private fun hashPassword(password: String): String {
        return java.security.MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
            .fold("", { str, it -> str + "%02x".format(it) })
    }
}
