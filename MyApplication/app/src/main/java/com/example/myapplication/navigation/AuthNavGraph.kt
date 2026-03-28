package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.repository.AnnouncementRepository
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.TaskRepository
import com.example.myapplication.ui.auth.LoginScreen
import com.example.myapplication.ui.auth.SignupScreen
import com.example.myapplication.ui.dashboard.AdminDashboard
import com.example.myapplication.ui.dashboard.EmployeeDashboard
import com.example.myapplication.ui.dashboard.HRDashboard
import com.example.myapplication.ui.dashboard.StatusCommunicationDashboard
import com.example.myapplication.viewmodel.AuthState
import com.example.myapplication.viewmodel.AuthViewModel
import com.example.myapplication.viewmodel.AuthViewModelFactory

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object AdminDashboard : Screen("admin_dashboard")
    object HRDashboard : Screen("hr_dashboard")
    object EmployeeDashboard : Screen("employee_dashboard")
    object StatusCommunication : Screen("status_communication")
}

@Composable
fun AuthNavGraph(
    authRepository: AuthRepository,
    taskRepository: TaskRepository,
    announcementRepository: AnnouncementRepository,
    navController: NavHostController = rememberNavController()
) {
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(authRepository, taskRepository, announcementRepository)
    )

    val authState by authViewModel.authState.collectAsState()
    val loginError by authViewModel.loginError.collectAsState()
    val registrationError by authViewModel.registrationError.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val tasks by authViewModel.tasks.collectAsState(initial = emptyList())
    val users by authViewModel.users.collectAsState(initial = emptyList())
    val announcements by authViewModel.announcements.collectAsState(initial = emptyList())

    // Determine start destination based on auth state
    val startDestination = when (authState) {
        is AuthState.Unauthenticated -> Screen.Login.route
        is AuthState.Authenticated.Admin -> Screen.AdminDashboard.route
        is AuthState.Authenticated.HR -> Screen.HRDashboard.route
        is AuthState.Authenticated.Employee -> Screen.EmployeeDashboard.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                isLoading = isLoading,
                errorMessage = loginError,
                onLoginSuccess = { user ->
                    authViewModel.clearErrors()
                    navController.navigate(Screen.AdminDashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignup = {
                    authViewModel.clearErrors()
                    navController.navigate(Screen.Signup.route)
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                authViewModel = authViewModel,
                isLoading = isLoading,
                errorMessage = registrationError,
                onSignupSuccess = {
                    authViewModel.clearErrors()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    authViewModel.clearErrors()
                    navController.navigateUp()
                }
            )
        }

        composable(Screen.AdminDashboard.route) {
            val adminUser = (authState as? AuthState.Authenticated.Admin)?.user
            adminUser?.let { user ->
                MainDashboardWithBottomNav(
                    currentUser = user,
                    tasks = tasks,
                    users = users,
                    announcements = announcements,
                    authViewModel = authViewModel,
                    navController = navController,
                    canCreateAnnouncement = true
                )
            }
        }

        composable(Screen.HRDashboard.route) {
            val hrUser = (authState as? AuthState.Authenticated.HR)?.user
            hrUser?.let { user ->
                MainDashboardWithBottomNav(
                    currentUser = user,
                    tasks = tasks,
                    users = users,
                    announcements = announcements,
                    authViewModel = authViewModel,
                    navController = navController,
                    canCreateAnnouncement = true
                )
            }
        }

        composable(Screen.EmployeeDashboard.route) {
            val employeeUser = (authState as? AuthState.Authenticated.Employee)?.user
            employeeUser?.let { user ->
                MainDashboardWithBottomNav(
                    currentUser = user,
                    tasks = tasks,
                    users = users,
                    announcements = announcements,
                    authViewModel = authViewModel,
                    navController = navController,
                    canCreateAnnouncement = false
                )
            }
        }

        composable(Screen.StatusCommunication.route) {
            val currentUser = (authState as? AuthState.Authenticated)?.user
            currentUser?.let { user ->
                StatusCommunicationDashboard(
                    currentUser = user,
                    announcements = announcements,
                    canCreateAnnouncement = authViewModel.canCreateAnnouncements(),
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
                    onRefresh = {
                        authViewModel.refreshData()
                    }
                )
            }
        }
    }
}
