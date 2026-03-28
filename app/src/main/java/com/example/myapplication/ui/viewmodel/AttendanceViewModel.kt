package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.model.Attendance
import com.example.myapplication.data.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AttendanceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AttendanceRepository

    val allAttendance: Flow<List<Attendance>>

    private val _currentAttendance = mutableStateOf<Attendance?>(null)
    val currentAttendance: State<Attendance?> = _currentAttendance

    private val _isClockedIn = mutableStateOf(false)
    val isClockedIn: State<Boolean> = _isClockedIn

    // Using a hardcoded employee ID for demo - replace with actual user ID from auth
    private val currentEmployeeId = "EMP001"

    init {
        val attendanceDao = AppDatabase.getDatabase(application).attendanceDao()
        repository = AttendanceRepository(attendanceDao)
        allAttendance = repository.getAllAttendance()
        checkCurrentStatus()
    }

    private fun checkCurrentStatus() {
        viewModelScope.launch {
            val active = repository.getActiveAttendance(currentEmployeeId)
            _currentAttendance.value = active
            _isClockedIn.value = active != null
        }
    }

    fun clockIn() {
        viewModelScope.launch {
            val attendance = repository.clockIn(currentEmployeeId)
            _currentAttendance.value = attendance
            _isClockedIn.value = true
        }
    }

    fun clockOut() {
        viewModelScope.launch {
            val attendance = repository.clockOut(currentEmployeeId)
            if (attendance != null) {
                _currentAttendance.value = null
                _isClockedIn.value = false
            }
        }
    }
}
