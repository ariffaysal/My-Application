package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myapplication.data.database.Converters

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
@TypeConverters(Converters::class)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val password: String,
    val role: UserRole = UserRole.EMPLOYEE,
    val department: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
