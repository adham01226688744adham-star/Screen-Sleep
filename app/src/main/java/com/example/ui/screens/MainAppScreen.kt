package com.example.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.SettingsSuggest
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BedtimeScheduleView
import com.example.ui.components.BedtimeWizardView
import com.example.ui.components.EyeBreakDialog
import com.example.ui.components.InsightsView
import com.example.ui.components.ScreenTimeDashboardView
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.EyeSage
import com.example.ui.theme.IndigoLight
import com.example.ui.theme.MidnightBlue
import com.example.ui.theme.TwilightDark
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MainAppScreen(
    viewModel: MainViewModel = viewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val currentLevel by viewModel.currentLevel.collectAsStateWithLifecycle()
    val timerSecondsRemaining by viewModel.timerSecondsRemaining.collectAsStateWithLifecycle()
    val isTimerRunning by viewModel.isTimerRunning.collectAsStateWithLifecycle()
    val isBreakActive by viewModel.isBreakActive.collectAsStateWithLifecycle()
    val breakSecondsRemaining by viewModel.breakSecondsRemaining.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val todaySteps by viewModel.todayBedtimeSteps.collectAsStateWithLifecycle()
    val todayBreaksCount by viewModel.todayBreaksCount.collectAsStateWithLifecycle()
    val hoursUntilBedtime by viewModel.hoursUntilBedtime.collectAsStateWithLifecycle()
    val minutesUntilBedtime by viewModel.minutesUntilBedtime.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MidnightBlue,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("bottom_navigation_bar"),
                containerColor = TwilightDark,
                contentColor = Color.White,
                tonalElevation = 6.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 0) Icons.Filled.Timer else Icons.Outlined.Timer,
                            contentDescription = "Screen Time"
                        )
                    },
                    label = { Text("Screen", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        indicatorColor = AmberGlow,
                        selectedTextColor = AmberGlow,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("tab_screen_time")
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 1) Icons.Filled.Bedtime else Icons.Outlined.Bedtime,
                            contentDescription = "Bedtime"
                        )
                    },
                    label = { Text("Bedtime", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        indicatorColor = IndigoLight,
                        selectedTextColor = IndigoLight,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("tab_bedtime_schedule")
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 2) Icons.Filled.SettingsSuggest else Icons.Outlined.SettingsSuggest,
                            contentDescription = "Sleep Setup"
                        )
                    },
                    label = { Text("Sleep Setup", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        indicatorColor = EyeSage,
                        selectedTextColor = EyeSage,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("tab_sleep_wizard")
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 3) Icons.Filled.Insights else Icons.Outlined.Insights,
                            contentDescription = "Insights"
                        )
                    },
                    label = { Text("Insights", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        indicatorColor = IndigoLight,
                        selectedTextColor = IndigoLight,
                        unselectedIconColor = Color.White.copy(alpha = 0.6f),
                        unselectedTextColor = Color.White.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("tab_insights")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> ScreenTimeDashboardView(
                    currentLevel = currentLevel,
                    timerSecondsRemaining = timerSecondsRemaining,
                    isTimerRunning = isTimerRunning,
                    todayBreaksCount = todayBreaksCount,
                    onToggleTimer = { viewModel.toggleTimer() },
                    onResetTimer = { viewModel.resetTimer() },
                    onStartBreakNow = { viewModel.startBreakSession() },
                    onSelectLevel = { lvl ->
                        viewModel.selectLevel(lvl)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                "Switched to ${lvl.title} (${lvl.ruleSummary})"
                            )
                        }
                    }
                )
                1 -> BedtimeScheduleView(
                    userProfile = userProfile,
                    steps = todaySteps,
                    hoursUntilBedtime = hoursUntilBedtime,
                    minutesUntilBedtime = minutesUntilBedtime,
                    onToggleStep = { stepId, completed ->
                        viewModel.toggleStepCompletion(stepId, completed)
                    },
                    onOpenWizard = { selectedTab = 2 }
                )
                2 -> BedtimeWizardView(
                    currentProfile = userProfile,
                    onSaveSchedule = { bHour, bMin, wHour, wMin, need, habit, windMin, lvlNum ->
                        viewModel.submitBedtimeNeeds(
                            bedHour = bHour,
                            bedMinute = bMin,
                            wakeHour = wHour,
                            wakeMinute = wMin,
                            sleepNeed = need,
                            screenHabit = habit,
                            windDownMin = windMin,
                            levelNumber = lvlNum
                        )
                        selectedTab = 1
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                "Personalized bedtime schedule updated!"
                            )
                        }
                    }
                )
                3 -> InsightsView(
                    todayBreaksCount = todayBreaksCount,
                    activeLevelNumber = currentLevel.levelNumber
                )
            }
        }
    }

    // Interactive 20-Second Eye Break Overlay
    if (isBreakActive) {
        EyeBreakDialog(
            level = currentLevel,
            secondsRemaining = breakSecondsRemaining,
            onDismiss = { viewModel.dismissBreakEarly() },
            onComplete = {
                viewModel.completeBreak()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Eye break logged! Great job giving your vision rest.")
                }
            }
        )
    }
}
