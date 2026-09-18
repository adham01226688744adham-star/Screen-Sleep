package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.UserProfileEntity
import com.example.data.model.ManagementLevel
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.EyeSage
import com.example.ui.theme.IndigoLight
import com.example.ui.theme.SlateNavy
import com.example.ui.theme.TwilightDark

@Composable
fun BedtimeWizardView(
    currentProfile: UserProfileEntity?,
    onSaveSchedule: (
        bedHour: Int,
        bedMinute: Int,
        wakeHour: Int,
        wakeMinute: Int,
        sleepNeed: String,
        screenHabit: String,
        windDownMin: Int,
        levelNumber: Int
    ) -> Unit
) {
    val scrollState = rememberScrollState()

    var selectedBedHour by remember { mutableIntStateOf(currentProfile?.bedTimeHour ?: 23) }
    var selectedBedMinute by remember { mutableIntStateOf(currentProfile?.bedTimeMinute ?: 0) }

    var selectedWakeHour by remember { mutableIntStateOf(currentProfile?.wakeHour ?: 7) }
    var selectedWakeMinute by remember { mutableIntStateOf(currentProfile?.wakeMinute ?: 0) }

    var selectedSleepNeed by remember {
        mutableStateOf(currentProfile?.sleepNeedChallenge ?: "Eye fatigue & screen headaches")
    }

    var selectedScreenHabit by remember {
        mutableStateOf(currentProfile?.screenHabit ?: "Using smartphone in bed in the dark")
    }

    var selectedWindDownMin by remember {
        mutableIntStateOf(currentProfile?.windDownMinutes ?: 45)
    }

    var selectedLevelNumber by remember {
        mutableIntStateOf(currentProfile?.selectedLevel ?: 2) // Default Level 2 per prompt
    }

    // Common bedtime presets
    val bedtimePresets = listOf(
        Pair(22, 0) to "10:00 PM",
        Pair(22, 30) to "10:30 PM",
        Pair(23, 0) to "11:00 PM",
        Pair(23, 30) to "11:30 PM",
        Pair(0, 0) to "12:00 AM",
        Pair(0, 30) to "12:30 AM"
    )

    // Common wake time presets
    val wakePresets = listOf(
        Pair(6, 0) to "6:00 AM",
        Pair(6, 30) to "6:30 AM",
        Pair(7, 0) to "7:00 AM",
        Pair(7, 30) to "7:30 AM",
        Pair(8, 0) to "8:00 AM",
        Pair(8, 30) to "8:30 AM"
    )

    val sleepNeedOptions = listOf(
        "Eye fatigue & screen headaches",
        "Late night doomscrolling in bed",
        "Hard to fall asleep (racing thoughts)",
        "Waking up groggy / low sleep quality"
    )

    val screenHabitOptions = listOf(
        "Using smartphone in bed in the dark",
        "Working late on laptop / computer",
        "Watching streaming shows / TV",
        "Casual social media before sleep"
    )

    val windDownOptions = listOf(
        30 to "30 Min: Quick & Focused",
        45 to "45 Min: Balanced (Recommended)",
        60 to "60 Min: Deep Digital Detox"
    )

    // Calculate duration & cycles
    var bedMinutes = selectedBedHour * 60 + selectedBedMinute
    var wakeMinutes = selectedWakeHour * 60 + selectedWakeMinute
    if (wakeMinutes <= bedMinutes) wakeMinutes += 24 * 60
    val totalSleepMinutes = wakeMinutes - bedMinutes
    val totalHours = totalSleepMinutes / 60
    val remMinutes = totalSleepMinutes % 60
    val cycles = totalSleepMinutes / 90

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("bedtime_wizard_screen")
    ) {
        Text(
            text = "Personalized Sleep Setup",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        Text(
            text = "Tell us your bedtime and habits to craft your custom circadian schedule.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White.copy(alpha = 0.7f)
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Question 1: Bedtime
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = TwilightDark),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bedtime,
                        contentDescription = null,
                        tint = IndigoLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "1. What time do you go to sleep?",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    bedtimePresets.take(3).forEach { (pair, label) ->
                        val isSelected = selectedBedHour == pair.first && selectedBedMinute == pair.second
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) IndigoLight else SlateNavy,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) Color.White else Color.White.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedBedHour = pair.first
                                    selectedBedMinute = pair.second
                                }
                                .testTag("bedtime_chip_${pair.first}_${pair.second}")
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    bedtimePresets.drop(3).forEach { (pair, label) ->
                        val isSelected = selectedBedHour == pair.first && selectedBedMinute == pair.second
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) IndigoLight else SlateNavy,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) Color.White else Color.White.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedBedHour = pair.first
                                    selectedBedMinute = pair.second
                                }
                                .testTag("bedtime_chip_${pair.first}_${pair.second}")
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question 2: Wake up time
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = TwilightDark),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = null,
                        tint = AmberGlow,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "2. What time do you wake up?",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    wakePresets.take(3).forEach { (pair, label) ->
                        val isSelected = selectedWakeHour == pair.first && selectedWakeMinute == pair.second
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) AmberGlow else SlateNavy,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) Color.White else Color.White.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedWakeHour = pair.first
                                    selectedWakeMinute = pair.second
                                }
                                .testTag("waketime_chip_${pair.first}_${pair.second}")
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.Black else Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    wakePresets.drop(3).forEach { (pair, label) ->
                        val isSelected = selectedWakeHour == pair.first && selectedWakeMinute == pair.second
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) AmberGlow else SlateNavy,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) Color.White else Color.White.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedWakeHour = pair.first
                                    selectedWakeMinute = pair.second
                                }
                                .testTag("waketime_chip_${pair.first}_${pair.second}")
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.Black else Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = EyeSage.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Estimated Sleep: ${totalHours}h ${remMinutes}m (~$cycles complete 90-minute sleep cycles)",
                        fontSize = 12.sp,
                        color = EyeSage,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(8.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question 3: Primary Need
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = TwilightDark),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = EyeSage,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "3. Your main bedtime challenge",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                sleepNeedOptions.forEach { option ->
                    val isSelected = selectedSleepNeed == option
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) IndigoLight.copy(alpha = 0.25f) else SlateNavy,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) IndigoLight else Color.White.copy(alpha = 0.08f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedSleepNeed = option }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.Check,
                                contentDescription = null,
                                tint = if (isSelected) IndigoLight else Color.White.copy(alpha = 0.3f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = option,
                                fontSize = 13.sp,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question 4: Screen Habits
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = TwilightDark),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "4. Night screen habit",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                screenHabitOptions.forEach { habit ->
                    val isSelected = selectedScreenHabit == habit
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) AmberGlow.copy(alpha = 0.2f) else SlateNavy,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) AmberGlow else Color.White.copy(alpha = 0.08f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedScreenHabit = habit }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.Check,
                                contentDescription = null,
                                tint = if (isSelected) AmberGlow else Color.White.copy(alpha = 0.3f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = habit,
                                fontSize = 13.sp,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question 5: Wind down length
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = TwilightDark),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "5. Wind-down buffer duration",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                windDownOptions.forEach { (mins, label) ->
                    val isSelected = selectedWindDownMin == mins
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) EyeSage.copy(alpha = 0.2f) else SlateNavy,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) EyeSage else Color.White.copy(alpha = 0.08f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedWindDownMin = mins }
                    ) {
                        Text(
                            text = label,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) EyeSage else Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question 6: Daytime Screen Level
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = TwilightDark),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = AmberGlow,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "6. Screen Time Management Level",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ManagementLevel.entries.forEach { level ->
                    val isSelected = selectedLevelNumber == level.levelNumber
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) {
                            if (level.levelNumber == 2) AmberGlow.copy(alpha = 0.2f) else IndigoLight.copy(alpha = 0.2f)
                        } else SlateNavy,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) {
                                if (level.levelNumber == 2) AmberGlow else IndigoLight
                            } else Color.White.copy(alpha = 0.08f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedLevelNumber = level.levelNumber }
                            .testTag("wizard_level_chip_${level.levelNumber}")
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = level.title,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        text = level.badge,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (level.levelNumber == 2) AmberGlow else IndigoLight,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = level.ruleSummary,
                                fontSize = 12.sp,
                                color = EyeSage
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Save & Generate Button
        Button(
            onClick = {
                onSaveSchedule(
                    selectedBedHour,
                    selectedBedMinute,
                    selectedWakeHour,
                    selectedWakeMinute,
                    selectedSleepNeed,
                    selectedScreenHabit,
                    selectedWindDownMin,
                    selectedLevelNumber
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("generate_schedule_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AmberGlow,
                contentColor = Color.Black
            )
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Generate Bedtime Schedule",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
