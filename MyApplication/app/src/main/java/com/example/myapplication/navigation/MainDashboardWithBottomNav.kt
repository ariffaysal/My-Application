package com.example.myapplication.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.myapplication.data.model.Announcement
import com.example.myapplication.data.model.Task
import com.example.myapplication.data.model.User
import com.example.myapplication.ui.components.TaskItem
import com.example.myapplication.ui.dashboard.AdminDashboard
import com.example.myapplication.ui.dashboard.EmployeeDashboard
import com.example.myapplication.ui.dashboard.HRDashboard
import com.example.myapplication.ui.dashboard.StatusCommunicationDashboard
import com.example.myapplication.viewmodel.AuthViewModel

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    object Dashboard : BottomNavItem("tasks", "Dashboard", Icons.Default.Dashboard)
    object Schedule : BottomNavItem("schedule", "Schedule", Icons.Default.Schedule)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}

@Composable
fun MainDashboardWithBottomNav(
    currentUser: User,
    tasks: List<Task>,
    users: List<User>,
    announcements: List<Announcement>,
    authViewModel: AuthViewModel,
    navController: NavHostController,
    canCreateAnnouncement: Boolean
) {
    var selectedTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Dashboard) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth()
            ) {
                val items = listOf(
                    BottomNavItem.Dashboard,
                    BottomNavItem.Schedule,
                    BottomNavItem.Profile
                )

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedTab == item,
                        onClick = { selectedTab = item }
                    )
                }
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            BottomNavItem.Dashboard -> {
                when (currentUser.role) {
                    com.example.myapplication.data.model.UserRole.ADMIN -> {
                        AdminDashboard(
                            currentUser = currentUser,
                            tasks = tasks,
                            users = users,
                            onLogout = {
                                authViewModel.logout()
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            onCreateTask = { /* TODO */ },
                            onManageUsers = { /* TODO */ },
                            onTaskClick = { /* TODO */ },
                            onAssignTask = { task, assigneeId ->
                                authViewModel.assignTask(task.id, assigneeId)
                            },
                            onRefresh = { authViewModel.refreshData() }
                        )
                    }
                    com.example.myapplication.data.model.UserRole.HR -> {
                        HRDashboard(
                            currentUser = currentUser,
                            tasks = tasks,
                            users = users,
                            onLogout = {
                                authViewModel.logout()
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            onCreateTask = { /* TODO */ },
                            onManageEmployees = { /* TODO */ },
                            onAssignTask = { /* TODO */ },
                            onTaskClick = { /* TODO */ },
                            onRefresh = { authViewModel.refreshData() }
                        )
                    }
                    com.example.myapplication.data.model.UserRole.EMPLOYEE -> {
                        EmployeeDashboard(
                            currentUser = currentUser,
                            tasks = tasks,
                            onLogout = {
                                authViewModel.logout()
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            onTaskStatusUpdate = { task, newStatus ->
                                authViewModel.updateTaskStatus(task.id, newStatus)
                            },
                            onRefresh = { authViewModel.refreshData() }
                        )
                    }
                    else -> { }
                }
            }
            BottomNavItem.Schedule -> {
                // Show schedule/tasks view with status dropdown
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items(tasks.size) { index ->
                        val task = tasks[index]
                        TaskItem(
                            task = task,
                            onClick = { },
                            showStatusDropdown = currentUser.role == com.example.myapplication.data.model.UserRole.EMPLOYEE,
                            onStatusChange = { newStatus ->
                                authViewModel.updateTaskStatus(task.id, newStatus)
                            }
                        )
                    }
                }
            }
            BottomNavItem.Profile -> {
                StatusCommunicationDashboard(
                    currentUser = currentUser,
                    announcements = announcements,
                    canCreateAnnouncement = canCreateAnnouncement,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onCreateAnnouncement = { title, content, priority ->
                        authViewModel.createAnnouncement(title, content, priority)
                    },
                    onDeleteAnnouncement = { announcement ->
                        authViewModel.deleteAnnouncement(announcement)
                    },
                    onRefresh = { authViewModel.refreshData() }
                )
            }
            else -> { }
        }
    }
}
