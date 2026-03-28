package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.myapplication.data.database.TaskDatabase
import com.example.myapplication.data.repository.AnnouncementRepository
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.TaskRepository
import com.example.myapplication.navigation.AuthNavGraph
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = TaskDatabase.getDatabase(this)
        val authRepository = AuthRepository(database.userDao())
        val taskRepository = TaskRepository(database.taskDao(), database.userDao())
        val announcementRepository = AnnouncementRepository(database.announcementDao())

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AuthNavGraph(
                    authRepository = authRepository,
                    taskRepository = taskRepository,
                    announcementRepository = announcementRepository
                )
            }
        }
    }
}
