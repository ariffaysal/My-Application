package com.example.myapplication.data.repository

import com.example.myapplication.data.local.AttendanceDao
import com.example.myapplication.data.model.Attendance
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class AttendanceRepository(
    private val attendanceDao: AttendanceDao
) {

    fun getAllAttendance(): Flow<List<Attendance>> = attendanceDao.getAllAttendance()

    fun getAttendanceByEmployee(employeeId: String): Flow<List<Attendance>> =
        attendanceDao.getAttendanceByEmployee(employeeId)

    suspend fun getActiveAttendance(employeeId: String): Attendance? =
        attendanceDao.getActiveAttendance(employeeId)

    suspend fun clockIn(employeeId: String): Attendance {
        val attendance = Attendance(
            id = UUID.randomUUID().toString(),
            employeeId = employeeId,
            checkInTime = System.currentTimeMillis(),
            checkOutTime = null,
            isSynced = false
        )
        attendanceDao.insert(attendance)
        return attendance
    }

    suspend fun clockOut(employeeId: String): Attendance? {
        val activeAttendance = attendanceDao.getActiveAttendance(employeeId)
        return activeAttendance?.let {
            val updated = it.copy(checkOutTime = System.currentTimeMillis())
            attendanceDao.update(updated)
            updated
        }
    }

    suspend fun markAsSynced(id: String) = attendanceDao.markAsSynced(id)

    suspend fun getUnsyncedAttendance(): List<Attendance> = attendanceDao.getUnsyncedAttendance()
}
