package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.model.Attendance
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attendance: Attendance)

    @Update
    suspend fun update(attendance: Attendance)

    @Query("SELECT * FROM attendance ORDER BY checkInTime DESC")
    fun getAllAttendance(): Flow<List<Attendance>>

    @Query("SELECT * FROM attendance WHERE isSynced = 0")
    suspend fun getUnsyncedAttendance(): List<Attendance>

    @Query("UPDATE attendance SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)

    @Query("SELECT * FROM attendance WHERE employeeId = :employeeId ORDER BY checkInTime DESC")
    fun getAttendanceByEmployee(employeeId: String): Flow<List<Attendance>>

    @Query("SELECT * FROM attendance WHERE employeeId = :employeeId AND checkOutTime IS NULL LIMIT 1")
    suspend fun getActiveAttendance(employeeId: String): Attendance?
}
