package com.example.myapplication.data.repository

import com.example.myapplication.data.database.TaskDao
import com.example.myapplication.data.database.UserDao
import com.example.myapplication.data.model.Task
import com.example.myapplication.data.model.TaskStatus
import com.example.myapplication.data.model.User
import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao,
    private val userDao: UserDao
) {

    val tasks: Flow<List<Task>> = taskDao.getAllTasks()
    val users: Flow<List<User>> = userDao.getAllUsers()

    suspend fun createTask(title: String, description: String, createdById: Long): Long {
        val newTask = Task(
            title = title.trim(),
            description = description.trim(),
            status = TaskStatus.PENDING,
            createdById = createdById
        )
        return taskDao.insertTask(newTask)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun updateTaskStatus(taskId: Long, status: TaskStatus) {
        taskDao.updateTaskStatus(taskId, status)
    }

    suspend fun assignTask(taskId: Long, userId: Long?) {
        taskDao.assignTask(taskId, userId)
    }

    suspend fun getTaskById(taskId: Long): Task? {
        return taskDao.getTaskById(taskId)
    }

    fun getTasksByAssignee(userId: Long): Flow<List<Task>> {
        return taskDao.getTasksByAssignee(userId)
    }

    fun getTasksByStatus(status: TaskStatus): Flow<List<Task>> {
        return taskDao.getTasksByStatus(status)
    }

    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }

    fun getUsersByRole(role: com.example.myapplication.data.model.UserRole): Flow<List<User>> {
        return userDao.getUsersByRole(role)
    }

    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }

    fun refreshData() {
        // Flows auto-refresh, but this can be used to trigger UI updates if needed
    }
}
