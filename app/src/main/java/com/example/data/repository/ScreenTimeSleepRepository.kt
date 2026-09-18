package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.db.BedtimeStepEntity
import com.example.data.db.BreakLogEntity
import com.example.data.db.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ScreenTimeSleepRepository(private val database: AppDatabase) {
    private val userProfileDao = database.userProfileDao()
    private val breakLogDao = database.breakLogDao()
    private val bedtimeStepDao = database.bedtimeStepDao()

    val userProfile: Flow<UserProfileEntity?> = userProfileDao.getUserProfile()
    val allBreakLogs: Flow<List<BreakLogEntity>> = breakLogDao.getAllBreakLogs()

    private fun getTodayDateTag(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getStartOfDayMillis(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    fun getTodayBreaksCount(): Flow<Int> {
        return breakLogDao.getTodayCompletedBreaksCount(getStartOfDayMillis())
    }

    fun getTodayBreakLogs(): Flow<List<BreakLogEntity>> {
        return breakLogDao.getTodayBreakLogs(getStartOfDayMillis())
    }

    fun getTodayBedtimeSteps(): Flow<List<BedtimeStepEntity>> {
        return bedtimeStepDao.getStepsForDate(getTodayDateTag())
    }

    suspend fun ensureInitialData() {
        val existingProfile = userProfileDao.getUserProfile().firstOrNull()
        val profile = existingProfile ?: UserProfileEntity(
            id = 1,
            selectedLevel = 2, // Level 2 default (20-20-20 rule)
            bedTimeHour = 23,
            bedTimeMinute = 0,
            wakeHour = 7,
            wakeMinute = 0,
            sleepNeedChallenge = "Eye fatigue & late phone usage",
            screenHabit = "Using phone in bed in the dark",
            windDownMinutes = 45,
            targetSleepCycles = 5,
            notificationsEnabled = true,
            hasCompletedAssessment = true
        )

        if (existingProfile == null) {
            userProfileDao.insertOrUpdateProfile(profile)
        }

        // Check if steps exist for today; if not, generate them
        val todayTag = getTodayDateTag()
        val currentSteps = bedtimeStepDao.getStepsForDate(todayTag).firstOrNull()
        if (currentSteps.isNullOrEmpty()) {
            generateAndSavePersonalizedSteps(profile, todayTag)
        }
    }

    suspend fun updateSelectedLevel(levelNumber: Int) {
        userProfileDao.updateLevel(levelNumber)
    }

    suspend fun recordCompletedBreak(levelNumber: Int, durationSeconds: Int) {
        val log = BreakLogEntity(
            timestamp = System.currentTimeMillis(),
            levelNumber = levelNumber,
            durationSeconds = durationSeconds,
            wasCompleted = true
        )
        breakLogDao.insertBreakLog(log)
    }

    suspend fun toggleBedtimeStep(stepId: Long, isCompleted: Boolean) {
        bedtimeStepDao.setStepCompleted(stepId, isCompleted)
    }

    suspend fun updateBedtimeAssessment(
        bedTimeHour: Int,
        bedTimeMinute: Int,
        wakeHour: Int,
        wakeMinute: Int,
        sleepNeedChallenge: String,
        screenHabit: String,
        windDownMinutes: Int,
        levelNumber: Int
    ) {
        // Calculate sleep duration in minutes
        var bedMinutes = bedTimeHour * 60 + bedTimeMinute
        var wakeMinutes = wakeHour * 60 + wakeMinute
        if (wakeMinutes <= bedMinutes) {
            wakeMinutes += 24 * 60 // next day
        }
        val totalSleepMinutes = wakeMinutes - bedMinutes
        val sleepCycles = (totalSleepMinutes / 90).coerceAtLeast(1)

        val updatedProfile = UserProfileEntity(
            id = 1,
            selectedLevel = levelNumber,
            bedTimeHour = bedTimeHour,
            bedTimeMinute = bedTimeMinute,
            wakeHour = wakeHour,
            wakeMinute = wakeMinute,
            sleepNeedChallenge = sleepNeedChallenge,
            screenHabit = screenHabit,
            windDownMinutes = windDownMinutes,
            targetSleepCycles = sleepCycles,
            notificationsEnabled = true,
            hasCompletedAssessment = true
        )
        userProfileDao.insertOrUpdateProfile(updatedProfile)

        // Regenerate steps for today
        val todayTag = getTodayDateTag()
        bedtimeStepDao.clearStepsForDate(todayTag)
        generateAndSavePersonalizedSteps(updatedProfile, todayTag)
    }

    private suspend fun generateAndSavePersonalizedSteps(
        profile: UserProfileEntity,
        dateTag: String
    ) {
        val steps = mutableListOf<BedtimeStepEntity>()

        // 1. Digital Sunset based on screen habit
        val screenCutoffTime = profile.windDownMinutes.coerceAtLeast(45)
        val screenDesc = when {
            profile.screenHabit.contains("bed", ignoreCase = true) ->
                "Stop phone usage in bed. Move charging cable away from your pillow to break unconscious reach reflex."
            profile.screenHabit.contains("laptop", ignoreCase = true) || profile.screenHabit.contains("work", ignoreCase = true) ->
                "Wrap up late work tabs, shut down your laptop, and avoid work email to stop cognitive screen hyper-arousal."
            else ->
                "Activate Blue Light Filter / Night Shift mode and put handheld screens away to protect melatonin release."
        }
        steps.add(
            BedtimeStepEntity(
                title = "Digital Sunset (Screens Off)",
                minutesBeforeBed = screenCutoffTime,
                description = screenDesc,
                category = "SCREEN",
                isCompletedToday = false,
                dateTag = dateTag
            )
        )

        // 2. Circadian Ambient Lighting
        val lightTime = (screenCutoffTime - 15).coerceAtLeast(30)
        steps.add(
            BedtimeStepEntity(
                title = "Amber Lighting Shift",
                minutesBeforeBed = lightTime,
                description = "Dim bright white overhead lights. Switch to warm amber bedside lamps (< 2700K) to signal your brain that night has fallen.",
                category = "LIGHT",
                isCompletedToday = false,
                dateTag = dateTag
            )
        )

        // 3. Eye Muscle & Physical Relaxation based on chosen challenge
        val eyeRelaxTime = 20
        val eyeDesc = when {
            profile.sleepNeedChallenge.contains("eye", ignoreCase = true) ->
                "Practice warm palming: rub your palms together until warm, gently rest them over your closed eyes for 60 seconds to soothe ciliary muscle spasms."
            profile.sleepNeedChallenge.contains("doomscrolling", ignoreCase = true) ->
                "Replace screen scrolling with a physical paperback book, calming notebook journaling, or soothing ambient soundscape."
            else ->
                "Perform gentle neck, shoulder, and upper-back stretches to undo the seated screen posture from your day."
        }
        steps.add(
            BedtimeStepEntity(
                title = "Ciliary Eye & Mind Wind-Down",
                minutesBeforeBed = eyeRelaxTime,
                description = eyeDesc,
                category = "RELAX",
                isCompletedToday = false,
                dateTag = dateTag
            )
        )

        // 4. Sleep Sanctuary Preparation
        steps.add(
            BedtimeStepEntity(
                title = "Sleep Sanctuary Setup",
                minutesBeforeBed = 10,
                description = "Ensure cool bedroom temperature (~18°C/65°F), close blackout blinds, and place a glass of water nearby.",
                category = "ENVIRONMENT",
                isCompletedToday = false,
                dateTag = dateTag
            )
        )

        // 5. Head on Pillow / Lights Out
        steps.add(
            BedtimeStepEntity(
                title = "Lights Out & Sleep Cycles",
                minutesBeforeBed = 0,
                description = "Turn off final lights. Settle into natural 90-minute REM/Deep sleep cycles for your targeted ${profile.targetSleepCycles} cycles.",
                category = "SLEEP",
                isCompletedToday = false,
                dateTag = dateTag
            )
        )

        bedtimeStepDao.insertSteps(steps)
    }
}
