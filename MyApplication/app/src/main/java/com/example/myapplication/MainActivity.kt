package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            enableEdgeToEdge()
            setContent {
                MyApplicationTheme {
                    // Test with simple version first
                    SimpleTest()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Ultimate fallback
            setContent {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("App started but crashed: ${e.message}")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTracker() {
    try {
        val tasks = remember { mutableStateListOf<Task>() }
        var newTaskTitle by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            "My Daily Tasks",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ) 
                    },
                    colors = TopAppBarDefaults.topAppBarColors()
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Task List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onTaskComplete = { updatedTask ->
                                val index = tasks.indexOf(task)
                                if (index != -1) {
                                    tasks[index] = updatedTask
                                }
                            },
                            onTaskDelete = {
                                tasks.remove(task)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Add Task Section
                AddTaskSection(
                    taskTitle = newTaskTitle,
                    onTaskTitleChange = { newTaskTitle = it },
                    onAddTask = {
                        if (newTaskTitle.isNotBlank()) {
                            tasks.add(
                                Task(
                                    id = System.currentTimeMillis().toString(),
                                    title = newTaskTitle.trim()
                                )
                            )
                            newTaskTitle = ""
                        }
                    }
                )
            }
        }
    } catch (e: Exception) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Error in TaskTracker: ${e.message}")
                Text("Check Logcat for details")
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onTaskComplete: (Task) -> Unit,
    onTaskDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.completed,
                    onCheckedChange = { isChecked ->
                        onTaskComplete(task.copy(completed = isChecked))
                    }
                )
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                Text(
                    text = task.title,
                    fontSize = 16.sp,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else null,
                    color = if (task.completed) 
                        androidx.compose.ui.graphics.Color.Gray 
                    else 
                        androidx.compose.ui.graphics.Color.Black
                )
            }
            
            IconButton(onClick = onTaskDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete task",
                    tint = androidx.compose.ui.graphics.Color.Red
                )
            }
        }
    }
}

@Composable
fun AddTaskSection(
    taskTitle: String,
    onTaskTitleChange: (String) -> Unit,
    onAddTask: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = taskTitle,
            onValueChange = onTaskTitleChange,
            modifier = Modifier.weight(1f),
            label = { Text("Enter new task") },
            singleLine = true
        )
        
        Button(
            onClick = onAddTask,
            enabled = taskTitle.isNotBlank()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add task"
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text("Add")
        }
    }
}