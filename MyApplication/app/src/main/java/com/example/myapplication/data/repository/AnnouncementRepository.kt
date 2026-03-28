package com.example.myapplication.data.repository

import com.example.myapplication.data.database.AnnouncementDao
import com.example.myapplication.data.model.Announcement
import com.example.myapplication.data.model.AnnouncementPriority
import com.example.myapplication.data.model.UserRole
import kotlinx.coroutines.flow.Flow

class AnnouncementRepository(private val announcementDao: AnnouncementDao) {

    val allAnnouncements: Flow<List<Announcement>> = announcementDao.getAllActiveAnnouncements()
    val highPriorityAnnouncements: Flow<List<Announcement>> = announcementDao.getHighPriorityAnnouncements()

    suspend fun createAnnouncement(
        title: String,
        content: String,
        authorId: Long,
        authorName: String,
        authorRole: UserRole,
        priority: AnnouncementPriority = AnnouncementPriority.NORMAL
    ): Long {
        val announcement = Announcement(
            title = title.trim(),
            content = content.trim(),
            authorId = authorId,
            authorName = authorName,
            authorRole = authorRole,
            priority = priority
        )
        return announcementDao.insertAnnouncement(announcement)
    }

    suspend fun updateAnnouncement(announcement: Announcement) {
        announcementDao.updateAnnouncement(announcement)
    }

    suspend fun deleteAnnouncement(announcement: Announcement) {
        announcementDao.deleteAnnouncement(announcement)
    }

    suspend fun deactivateAnnouncement(announcementId: Long) {
        announcementDao.setAnnouncementActiveStatus(announcementId, false)
    }

    suspend fun getAnnouncementById(id: Long): Announcement? {
        return announcementDao.getAnnouncementById(id)
    }

    fun getAnnouncementsByAuthor(authorId: Long): Flow<List<Announcement>> {
        return announcementDao.getAnnouncementsByAuthor(authorId)
    }

    fun getAnnouncementsByPriority(priority: AnnouncementPriority): Flow<List<Announcement>> {
        return announcementDao.getAnnouncementsByPriority(priority)
    }

    fun canCreateAnnouncement(role: UserRole): Boolean {
        return role == UserRole.ADMIN || role == UserRole.HR
    }
}
