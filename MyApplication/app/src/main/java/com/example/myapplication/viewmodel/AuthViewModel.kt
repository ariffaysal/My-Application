package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Announcement
import com.example.myapplication.data.model.AnnouncementPriority
import com.example.myapplication.data.model.TaskStatus
import com.example.myapplication.data.model.User
import com.example.myapplication.data.model.UserRole
import com.example.myapplication.data.repository.AnnouncementRepository
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val taskRepository: TaskRepository,
    private val announcementRepository: AnnouncementRepository
) : ViewModel() {

    val currentUser = authRepository.currentUser

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _registrationError = MutableStateFlow<String?>(null)
    val registrationError: StateFlow<String?> = _registrationError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            currentUser.collect { user ->
                _authState.value = when {
                    user == null -> AuthState.Unauthenticated
                    user.role == UserRole.ADMIN -> AuthState.Authenticated.Admin(user)
                    user.role == UserRole.HR -> AuthState.Authenticated.HR(user)
                    user.role == UserRole.EMPLOYEE -> AuthState.Authenticated.Employee(user)
                    else -> AuthState.Unauthenticated
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginError.value = null

            val result = authRepository.login(email, password)
            result.onFailure { error ->
                _loginError.value = error.message ?: "Login failed"
            }

            _isLoading.value = false
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        role: UserRole,
        department: String = ""
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _registrationError.value = null

            val result = authRepository.register(name, email, password, role, department)
            result.onFailure { error ->
                _registrationError.value = error.message ?: "Registration failed"
            }

            _isLoading.value = false
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun clearErrors() {
        _loginError.value = null
        _registrationError.value = null
    }

    // Task operations delegated to TaskRepository
    val tasks = taskRepository.tasks
    val users = taskRepository.users

    // Announcement operations
    val announcements = announcementRepository.allAnnouncements

    fun refreshData() {
        viewModelScope.launch {
            taskRepository.refreshData()
        }
    }

    fun canCreateAnnouncements(): Boolean {
        return currentUser.value?.let { user ->
            announcementRepository.canCreateAnnouncement(user.role)
        } ?: false
    }

    fun createAnnouncement(title: String, content: String, priority: AnnouncementPriority) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            announcementRepository.createAnnouncement(
                title = title,
                content = content,
                authorId = user.id,
                authorName = user.name,
                authorRole = user.role,
                priority = priority
            )
        }
    }

    fun deleteAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            announcementRepository.deleteAnnouncement(announcement)
        }
    }

    fun createTask(title: String, description: String, createdById: Long) {
        viewModelScope.launch {
            taskRepository.createTask(title, description, createdById)
        }
    }

    fun updateTaskStatus(taskId: Long, status: TaskStatus) {
        viewModelScope.launch {
            taskRepository.updateTaskStatus(taskId, status)
        }
    }

    fun assignTask(taskId: Long, userId: Long?) {
        viewModelScope.launch {
            taskRepository.assignTask(taskId, userId)
        }
    }
}

sealed class AuthState {
    object Unauthenticated : AuthState()
    sealed class Authenticated(val user: User) : AuthState() {
        class Admin(user: User) : Authenticated(user)
        class HR(user: User) : Authenticated(user)
        class Employee(user: User) : Authenticated(user)
    }
}

class AuthViewModelFactory(
    private val authRepository: AuthRepository,
    private val taskRepository: TaskRepository,
    private val announcementRepository: AnnouncementRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authRepository, taskRepository, announcementRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
