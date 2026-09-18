package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ManagementLevel
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.EyeSage
import com.example.ui.theme.IndigoContainer
import com.example.ui.theme.IndigoLight
import com.example.ui.theme.SlateNavy
import com.example.ui.theme.TwilightDark

@Composable
fun ScreenTimeDashboardView(
    currentLevel: ManagementLevel,
    timerSecondsRemaining: Int,
    isTimerRunning: Boolean,
    todayBreaksCount: Int,
    onToggleTimer: () -> Unit,
    onResetTimer: () -> Unit,
    onStartBreakNow: () -> Unit,
    onSelectLevel: (ManagementLevel) -> Unit
) {
    val scrollState = rememberScrollState()

    val totalIntervalSeconds = (currentLevel.intervalMinutes * 60).coerceAtLeast(1)
    val progress = (totalIntervalSeconds - timerSecondsRemaining).toFloat() / totalIntervalSeconds.toFloat()

    val minutes = timerSecondsRemaining / 60
    val seconds = timerSecondsRemaining % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("screen_time_dashboard")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Screen Interval Guard",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
                Text(
                    text = "Ciliary Muscle & Strain Prevention",
                    fontSize = 12.sp,
                    color = AmberGlow
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = EyeSage.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, EyeSage.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = EyeSage,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$todayBreaksCount breaks today",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = EyeSage
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Big Interval Timer Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = TwilightDark),
            border = BorderStroke(1.5.dp, if (currentLevel.levelNumber == 2) AmberGlow.copy(alpha = 0.4f) else IndigoLight.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Active Level Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (currentLevel.levelNumber == 2) AmberGlow.copy(alpha = 0.18f) else IndigoLight.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "ACTIVE: ${currentLevel.title.uppercase()}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                        color = if (currentLevel.levelNumber == 2) AmberGlow else IndigoLight,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Circular Progress Timer
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(210.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.size(200.dp),
                        color = if (currentLevel.levelNumber == 2) AmberGlow else IndigoLight,
                        trackColor = Color.White.copy(alpha = 0.08f),
                        strokeWidth = 10.dp
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = timeFormatted,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            modifier = Modifier.testTag("interval_timer_display")
                        )
                        Text(
                            text = "UNTIL NEXT REST",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentLevel.distanceDescription,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = EyeSage
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Controls Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onResetTimer,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(SlateNavy)
                            .testTag("reset_timer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset Timer",
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Button(
                        onClick = onToggleTimer,
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("toggle_timer_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isTimerRunning) SlateNavy else EyeSage,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isTimerRunning) "Pause Session" else "Resume Focus")
                    }

                    Button(
                        onClick = onStartBreakNow,
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("take_break_now_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmberGlow,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Take Break", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 20-20-20 Rule Highlight Box
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = IndigoContainer.copy(alpha = 0.4f),
            border = BorderStroke(1.dp, IndigoLight.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = IndigoLight,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "The 20-20-20 rule (Level 2): Every 20 minutes, look at an object 20 feet (6m) away for 20 seconds to relax your eye muscles.",
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Level Selection Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Screen Time Levels",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Text(
                text = "Tap to switch level",
                fontSize = 11.5.sp,
                color = Color.White.copy(alpha = 0.5f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        ManagementLevel.entries.forEach { level ->
            LevelCard(
                level = level,
                isSelected = currentLevel == level,
                onSelect = { onSelectLevel(level) },
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
