package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.BedtimeStepEntity
import com.example.data.db.UserProfileEntity
import com.example.data.model.ManagementLevel
import com.example.data.repository.ScreenTimeSleepRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val repository = ScreenTimeSleepRepository(database)

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val todayBedtimeSteps: StateFlow<List<BedtimeStepEntity>> = repository.getTodayBedtimeSteps()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayBreaksCount: StateFlow<Int> = repository.getTodayBreaksCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Current selected level
    private val _currentLevel = MutableStateFlow(ManagementLevel.LEVEL_2)
    val currentLevel: StateFlow<ManagementLevel> = _currentLevel.asStateFlow()

    // Screen Time Interval Countdown (e.g. 20 mins for Level 2)
    private val _timerSecondsRemaining = MutableStateFlow(20 * 60)
    val timerSecondsRemaining: StateFlow<Int> = _timerSecondsRemaining.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(true)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private var timerJob: Job? = null

    // Eye Break Interactive Mode
    private val _isBreakActive = MutableStateFlow(false)
    val isBreakActive: StateFlow<Boolean> = _isBreakActive.asStateFlow()

    private val _breakSecondsRemaining = MutableStateFlow(20)
    val breakSecondsRemaining: StateFlow<Int> = _breakSecondsRemaining.asStateFlow()

    private var breakJob: Job? = null

    // Bedtime Time remaining
    private val _hoursUntilBedtime = MutableStateFlow(0)
    val hoursUntilBedtime: StateFlow<Int> = _hoursUntilBedtime.asStateFlow()

    private val _minutesUntilBedtime = MutableStateFlow(0)
    val minutesUntilBedtime: StateFlow<Int> = _minutesUntilBedtime.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureInitialData()
            repository.userProfile.collect { profile ->
                if (profile != null) {
                    val lvl = ManagementLevel.fromLevelNumber(profile.selectedLevel)
                    _currentLevel.value = lvl
                    calculateBedtimeCountdown(profile.bedTimeHour, profile.bedTimeMinute)
                }
            }
        }
        startScreenIntervalTimer()
    }

    fun selectLevel(level: ManagementLevel) {
        _currentLevel.value = level
        resetScreenIntervalTimer(level.intervalMinutes * 60)
        viewModelScope.launch {
            repository.updateSelectedLevel(level.levelNumber)
        }
    }

    fun toggleTimer() {
        if (_isTimerRunning.value) {
            pauseScreenIntervalTimer()
        } else {
            startScreenIntervalTimer()
        }
    }

    fun resetTimer() {
        resetScreenIntervalTimer(_currentLevel.value.intervalMinutes * 60)
    }

    private fun startScreenIntervalTimer() {
        _isTimerRunning.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_isTimerRunning.value) {
                delay(1000L)
                if (_timerSecondsRemaining.value > 0) {
                    _timerSecondsRemaining.value -= 1
                } else {
                    // Interval reached! Trigger Eye Break!
                    triggerVibration()
                    startBreakSession()
                    break
                }
            }
        }
    }

    private fun pauseScreenIntervalTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
    }

    private fun resetScreenIntervalTimer(seconds: Int) {
        _timerSecondsRemaining.value = seconds
        if (_isTimerRunning.value) {
            startScreenIntervalTimer()
        }
    }

    fun startBreakSession() {
        pauseScreenIntervalTimer()
        val duration = _currentLevel.value.breakSeconds
        _breakSecondsRemaining.value = duration
        _isBreakActive.value = true

        breakJob?.cancel()
        breakJob = viewModelScope.launch {
            while (_breakSecondsRemaining.value > 0) {
                delay(1000L)
                _breakSecondsRemaining.value -= 1
            }
            // Finished break!
            triggerVibration()
            completeBreak()
        }
    }

    fun dismissBreakEarly() {
        breakJob?.cancel()
        _isBreakActive.value = false
        resetScreenIntervalTimer(_currentLevel.value.intervalMinutes * 60)
    }

    fun completeBreak() {
        breakJob?.cancel()
        _isBreakActive.value = false
        val level = _currentLevel.value
        viewModelScope.launch {
            repository.recordCompletedBreak(level.levelNumber, level.breakSeconds)
        }
        resetScreenIntervalTimer(level.intervalMinutes * 60)
    }

    fun toggleStepCompletion(stepId: Long, completed: Boolean) {
        viewModelScope.launch {
            repository.toggleBedtimeStep(stepId, completed)
        }
    }

    fun submitBedtimeNeeds(
        bedHour: Int,
        bedMinute: Int,
        wakeHour: Int,
        wakeMinute: Int,
        sleepNeed: String,
        screenHabit: String,
        windDownMin: Int,
        levelNumber: Int
    ) {
        viewModelScope.launch {
            repository.updateBedtimeAssessment(
                bedTimeHour = bedHour,
                bedTimeMinute = bedMinute,
                wakeHour = wakeHour,
                wakeMinute = wakeMinute,
                sleepNeedChallenge = sleepNeed,
                screenHabit = screenHabit,
                windDownMinutes = windDownMin,
                levelNumber = levelNumber
            )
            _currentLevel.value = ManagementLevel.fromLevelNumber(levelNumber)
            resetScreenIntervalTimer(_currentLevel.value.intervalMinutes * 60)
            calculateBedtimeCountdown(bedHour, bedMinute)
        }
    }

    private fun calculateBedtimeCountdown(bedHour: Int, bedMinute: Int) {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, bedHour)
            set(Calendar.MINUTE, bedMinute)
            set(Calendar.SECOND, 0)
        }
        if (target.before(now)) {
            target.add(Calendar.DAY_OF_YEAR, 1)
        }
        val diffMillis = target.timeInMillis - now.timeInMillis
        val diffMinutes = (diffMillis / (1000 * 60)).toInt()
        _hoursUntilBedtime.value = (diffMinutes / 60).coerceAtLeast(0)
        _minutesUntilBedtime.value = (diffMinutes % 60).coerceAtLeast(0)
    }

    private fun triggerVibration() {
        try {
            val context = getApplication<Application>().applicationContext
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(400)
            }
        } catch (_: Exception) {
            // Graceful fallback if vibration not available
        }
    }
}
