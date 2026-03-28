package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance")
data class Attendance(
    @PrimaryKey
    val id: String,
    val employeeId: String,
    val checkInTime: Long,
    val checkOutTime: Long? = null,
    val isSynced: Boolean = false
)
