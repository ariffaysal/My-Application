package com.example.myapplication.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.model.Announcement
import com.example.myapplication.data.model.AnnouncementPriority
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {

    @Query("SELECT * FROM announcements WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActiveAnnouncements(): Flow<List<Announcement>>

    @Query("SELECT * FROM announcements ORDER BY createdAt DESC")
    fun getAllAnnouncements(): Flow<List<Announcement>>

    @Query("SELECT * FROM announcements WHERE id = :id")
    suspend fun getAnnouncementById(id: Long): Announcement?

    @Query("SELECT * FROM announcements WHERE authorId = :authorId ORDER BY createdAt DESC")
    fun getAnnouncementsByAuthor(authorId: Long): Flow<List<Announcement>>

    @Query("SELECT * FROM announcements WHERE priority = :priority AND isActive = 1 ORDER BY createdAt DESC")
    fun getAnnouncementsByPriority(priority: AnnouncementPriority): Flow<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: Announcement): Long

    @Update
    suspend fun updateAnnouncement(announcement: Announcement)

    @Delete
    suspend fun deleteAnnouncement(announcement: Announcement)

    @Query("DELETE FROM announcements WHERE id = :id")
    suspend fun deleteAnnouncementById(id: Long)

    @Query("UPDATE announcements SET isActive = :isActive WHERE id = :announcementId")
    suspend fun setAnnouncementActiveStatus(announcementId: Long, isActive: Boolean)

    @Query("SELECT * FROM announcements WHERE priority IN ('HIGH', 'URGENT') AND isActive = 1 ORDER BY createdAt DESC LIMIT 5")
    fun getHighPriorityAnnouncements(): Flow<List<Announcement>>
}
