package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val selectedLevel: Int = 2,
    val bedTimeHour: Int = 23,
    val bedTimeMinute: Int = 0,
    val wakeHour: Int = 7,
    val wakeMinute: Int = 0,
    val sleepNeedChallenge: String = "Eye fatigue & late phone usage",
    val screenHabit: String = "Using phone in bed in the dark",
    val windDownMinutes: Int = 45,
    val targetSleepCycles: Int = 5, // 5 cycles * 90m = 7.5 hrs
    val notificationsEnabled: Boolean = true,
    val hasCompletedAssessment: Boolean = true
)

@Entity(tableName = "break_logs")
data class BreakLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val levelNumber: Int,
    val durationSeconds: Int,
    val wasCompleted: Boolean = true
)

@Entity(tableName = "bedtime_steps")
data class BedtimeStepEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val minutesBeforeBed: Int,
    val description: String,
    val category: String, // "SCREEN", "LIGHT", "RELAX", "ENVIRONMENT", "SLEEP"
    val isCompletedToday: Boolean = false,
    val dateTag: String // e.g. "2026-09-18"
)
