package com.example.myapplication.data.database

import androidx.room.TypeConverter
import com.example.myapplication.data.model.AnnouncementPriority
import com.example.myapplication.data.model.TaskStatus
import com.example.myapplication.data.model.UserRole

class Converters {
    @TypeConverter
    fun fromTaskStatus(status: TaskStatus): String {
        return status.name
    }

    @TypeConverter
    fun toTaskStatus(status: String): TaskStatus {
        return TaskStatus.valueOf(status)
    }

    @TypeConverter
    fun fromUserRole(role: UserRole): String {
        return role.name
    }

    @TypeConverter
    fun toUserRole(role: String): UserRole {
        return UserRole.valueOf(role)
    }

    @TypeConverter
    fun fromAnnouncementPriority(priority: AnnouncementPriority): String {
        return priority.name
    }

    @TypeConverter
    fun toAnnouncementPriority(priority: String): AnnouncementPriority {
        return AnnouncementPriority.valueOf(priority)
    }
}
