package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val authorId: Long,
    val authorName: String,
    val authorRole: UserRole,
    val priority: AnnouncementPriority = AnnouncementPriority.NORMAL,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)

enum class AnnouncementPriority {
    LOW,
    NORMAL,
    HIGH,
    URGENT
}
