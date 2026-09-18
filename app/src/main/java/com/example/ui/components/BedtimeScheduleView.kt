package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.db.BedtimeStepEntity
import com.example.data.db.UserProfileEntity
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.AmberWarm
import com.example.ui.theme.EyeSage
import com.example.ui.theme.IndigoLight
import com.example.ui.theme.MidnightBlue
import com.example.ui.theme.SlateNavy
import com.example.ui.theme.TwilightDark

@Composable
fun BedtimeScheduleView(
    userProfile: UserProfileEntity?,
    steps: List<BedtimeStepEntity>,
    hoursUntilBedtime: Int,
    minutesUntilBedtime: Int,
    onToggleStep: (Long, Boolean) -> Unit,
    onOpenWizard: () -> Unit
) {
    val scrollState = rememberScrollState()

    val bedHour = userProfile?.bedTimeHour ?: 23
    val bedMinute = userProfile?.bedTimeMinute ?: 0
    val wakeHour = userProfile?.wakeHour ?: 7
    val wakeMinute = userProfile?.wakeMinute ?: 0
    val cycles = userProfile?.targetSleepCycles ?: 5

    fun formatTime(h: Int, m: Int): String {
        val amPm = if (h >= 12) "PM" else "AM"
        val hour12 = if (h % 12 == 0) 12 else h % 12
        return String.format("%d:%02d %s", hour12, m, amPm)
    }

    fun calculateStepClockTime(minutesBeforeBed: Int): String {
        var totalMinutes = (bedHour * 60 + bedMinute) - minutesBeforeBed
        if (totalMinutes < 0) totalMinutes += 24 * 60
        val h = (totalMinutes / 60) % 24
        val m = totalMinutes % 60
        return formatTime(h, m)
    }

    val completedCount = steps.count { it.isCompletedToday }
    val progressFraction = if (steps.isNotEmpty()) completedCount.toFloat() / steps.size else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("bedtime_schedule_screen")
    ) {
        // Hero Image Banner
        Card(
            shape = RoundedCornerShape(22.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            colors = CardDefaults.cardColors(containerColor = TwilightDark)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.hero_sleep_banner),
                    contentDescription = "Twilight Sleep Sanctuary",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    MidnightBlue.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Tonight's Circadian Rhythm",
                        color = AmberGlow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Target Bedtime: ${formatTime(bedHour, bedMinute)}",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Countdown & Sleep Cycles Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = TwilightDark),
            border = androidx.compose.foundation.BorderStroke(1.dp, IndigoLight.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "COUNTDOWN TO BEDTIME",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndigoLight,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${hoursUntilBedtime}h ${minutesUntilBedtime}m away",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = onOpenWizard,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SlateNavy,
                            contentColor = AmberGlow
                        ),
                        modifier = Modifier.testTag("edit_bedtime_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Customize", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = SlateNavy.copy(alpha = 0.6f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Bedtime,
                                    contentDescription = null,
                                    tint = IndigoLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Bedtime",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                            Text(
                                text = formatTime(bedHour, bedMinute),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = SlateNavy.copy(alpha = 0.6f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.WbSunny,
                                    contentDescription = null,
                                    tint = AmberWarm,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Wake Up",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                            Text(
                                text = formatTime(wakeHour, wakeMinute),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = SlateNavy.copy(alpha = 0.6f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Cycles",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "$cycles (90m ea)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = EyeSage
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Sleep cycles of 90 minutes ensure you wake during light stage sleep without morning brain fog.",
                    fontSize = 11.5.sp,
                    color = Color.White.copy(alpha = 0.65f),
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bedtime Routine Progress Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Personalized Bedtime Routine",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = "$completedCount of ${steps.size} steps completed tonight",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = EyeSage.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "${(progressFraction * 100).toInt()}% Ready",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = EyeSage,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progressFraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = EyeSage,
            trackColor = Color.White.copy(alpha = 0.1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Steps list
        steps.forEachIndexed { index, step ->
            val icon = when (step.category) {
                "SCREEN" -> Icons.Default.PhoneAndroid
                "LIGHT" -> Icons.Default.Lightbulb
                "RELAX" -> Icons.Default.SelfImprovement
                "ENVIRONMENT" -> Icons.Default.Nightlight
                else -> Icons.Default.Bedtime
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (step.isCompletedToday) TwilightDark.copy(alpha = 0.5f) else TwilightDark
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (step.isCompletedToday) EyeSage.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.08f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable { onToggleStep(step.id, !step.isCompletedToday) }
                    .testTag("bedtime_step_${step.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = if (step.isCompletedToday) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = if (step.isCompletedToday) "Completed" else "Not completed",
                        tint = if (step.isCompletedToday) EyeSage else Color.White.copy(alpha = 0.4f),
                        modifier = Modifier
                            .size(24.dp)
                            .padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = step.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp,
                                color = if (step.isCompletedToday) Color.White.copy(alpha = 0.6f) else Color.White
                            )

                            Text(
                                text = calculateStepClockTime(step.minutesBeforeBed),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberGlow
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = step.description,
                            fontSize = 12.5.sp,
                            lineHeight = 17.sp,
                            color = Color.White.copy(alpha = if (step.isCompletedToday) 0.5f else 0.8f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
