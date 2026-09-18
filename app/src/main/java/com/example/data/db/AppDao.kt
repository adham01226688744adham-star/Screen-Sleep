package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET selectedLevel = :level WHERE id = 1")
    suspend fun updateLevel(level: Int)

    @Query("UPDATE user_profile SET bedTimeHour = :hour, bedTimeMinute = :minute WHERE id = 1")
    suspend fun updateBedtime(hour: Int, minute: Int)

    @Query("UPDATE user_profile SET wakeHour = :hour, wakeMinute = :minute WHERE id = 1")
    suspend fun updateWakeTime(hour: Int, minute: Int)
}

@Dao
interface BreakLogDao {
    @Query("SELECT * FROM break_logs ORDER BY timestamp DESC")
    fun getAllBreakLogs(): Flow<List<BreakLogEntity>>

    @Query("SELECT * FROM break_logs WHERE timestamp >= :startOfDayTimestamp ORDER BY timestamp DESC")
    fun getTodayBreakLogs(startOfDayTimestamp: Long): Flow<List<BreakLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreakLog(log: BreakLogEntity)

    @Query("SELECT COUNT(*) FROM break_logs WHERE timestamp >= :startOfDayTimestamp AND wasCompleted = 1")
    fun getTodayCompletedBreaksCount(startOfDayTimestamp: Long): Flow<Int>
}

@Dao
interface BedtimeStepDao {
    @Query("SELECT * FROM bedtime_steps WHERE dateTag = :dateTag ORDER BY minutesBeforeBed DESC")
    fun getStepsForDate(dateTag: String): Flow<List<BedtimeStepEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<BedtimeStepEntity>)

    @Query("UPDATE bedtime_steps SET isCompletedToday = :completed WHERE id = :stepId")
    suspend fun setStepCompleted(stepId: Long, completed: Boolean)

    @Query("DELETE FROM bedtime_steps WHERE dateTag = :dateTag")
    suspend fun clearStepsForDate(dateTag: String)
}
