package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ManagementLevel
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.EyeSage
import com.example.ui.theme.IndigoLight
import com.example.ui.theme.MidnightBlue
import com.example.ui.theme.TwilightDark

@Composable
fun EyeBreakDialog(
    level: ManagementLevel,
    secondsRemaining: Int,
    onDismiss: () -> Unit,
    onComplete: () -> Unit
) {
    val totalSeconds = level.breakSeconds.coerceAtLeast(1)
    val progress = (totalSeconds - secondsRemaining).toFloat() / totalSeconds.toFloat()

    // Breathing / focus distance pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.14f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("eye_break_dialog"),
            color = MidnightBlue.copy(alpha = 0.96f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Top close button
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .testTag("close_break_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss Break Early",
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp, bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = EyeSage.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EyeSage.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = EyeSage,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Active Eye Rest: ${level.title}",
                                    color = EyeSage,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (level.levelNumber == 2) {
                                "Look 20 Feet (~6m) Away"
                            } else {
                                "Rest Your Focus"
                            },
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Shift gaze to a distant point out a window or across the room",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White.copy(alpha = 0.7f)
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }

                    // Center Focus Animation & Countdown
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(260.dp)
                    ) {
                        // Outer distance focal ripple
                        Box(
                            modifier = Modifier
                                .size(240.dp)
                                .scale(pulseScale)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(
                                            IndigoLight.copy(alpha = 0.25f),
                                            AmberGlow.copy(alpha = 0.08f),
                                            Color.Transparent
                                        )
                                    )
                                )
                                .border(1.5.dp, IndigoLight.copy(alpha = 0.35f), CircleShape)
                        )

                        // Circular timer progress
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.size(210.dp),
                            color = AmberGlow,
                            trackColor = Color.White.copy(alpha = 0.12f),
                            strokeWidth = 6.dp
                        )

                        // Inner focal circle
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .size(160.dp)
                                .clip(CircleShape)
                                .background(TwilightDark)
                                .border(2.dp, AmberGlow.copy(alpha = 0.6f), CircleShape)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "${secondsRemaining}s",
                                fontSize = 44.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "REMAINING",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberGlow,
                                letterSpacing = 1.2.sp
                            )
                        }
                    }

                    // Guidance Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = TwilightDark.copy(alpha = 0.9f)),
                        shape = RoundedCornerShape(18.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Ophthalmology Rule",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = EyeSage
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "${level.intervalMinutes}m interval",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = level.detailedGuidance,
                                fontSize = 12.5.sp,
                                lineHeight = 18.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• Blink softly 5 times to refresh corneal moisture\n• Keep your neck aligned without tilting forward",
                                fontSize = 11.5.sp,
                                lineHeight = 16.sp,
                                color = AmberGlow.copy(alpha = 0.9f)
                            )
                        }
                    }

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("skip_break_button"),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                        ) {
                            Text("Skip For Now", color = Color.White.copy(alpha = 0.8f))
                        }

                        Button(
                            onClick = onComplete,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("finish_break_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EyeSage,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("I've Finished", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
