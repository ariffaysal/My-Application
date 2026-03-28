package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class User(
    @PrimaryKey
    val id: String,
    val fullName: String,
    val email: String,
    val passwordHash: String,
    val role: String
)
