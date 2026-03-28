package com.example.myapplication.data.repository

import com.example.myapplication.data.database.UserDao
import com.example.myapplication.data.model.User
import com.example.myapplication.data.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepository(private val userDao: UserDao) {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val user = userDao.getUserByEmail(email.trim())
            if (user == null) {
                Result.failure(Exception("User not found"))
            } else if (user.password != password) {
                Result.failure(Exception("Invalid password"))
            } else if (!user.isActive) {
                Result.failure(Exception("Account is deactivated. Contact HR."))
            } else {
                _currentUser.value = user
                Result.success(user)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        name: String,
        email: String,
        password: String,
        role: UserRole,
        department: String = ""
    ): Result<User> {
        return try {
            // Check if email already exists
            val existingUser = userDao.getUserByEmail(email.trim())
            if (existingUser != null) {
                return Result.failure(Exception("Email already registered"))
            }

            val newUser = User(
                name = name.trim(),
                email = email.trim(),
                password = password,
                role = role,
                department = department.trim()
            )

            val userId = userDao.insertUser(newUser)
            val createdUser = newUser.copy(id = userId)
            Result.success(createdUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    fun isLoggedIn(): Boolean {
        return _currentUser.value != null
    }

    fun getCurrentUserRole(): UserRole? {
        return _currentUser.value?.role
    }

    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }

    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }
}
