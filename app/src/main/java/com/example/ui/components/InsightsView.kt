package com.example.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.EyeSage
import com.example.ui.theme.IndigoLight
import com.example.ui.theme.SlateNavy
import com.example.ui.theme.TwilightDark

@Composable
fun InsightsView(
    todayBreaksCount: Int,
    activeLevelNumber: Int
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("insights_screen")
    ) {
        Text(
            text = "Science of Rest & Vision",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        Text(
            text = "Clinical principles behind screen breaks and circadian sleep hygiene.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White.copy(alpha = 0.7f)
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Daily Stat Highlights
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                color = TwilightDark,
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "TODAY'S BREAKS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EyeSage,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$todayBreaksCount",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "completed rests",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }

            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                color = TwilightDark,
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "CURRENT LEVEL",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberGlow,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Level $activeLevelNumber",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = if (activeLevelNumber == 2) "20-20-20 Rule" else "Active cadence",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section 1: The 20-20-20 Rule Science
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = TwilightDark),
            border = BorderStroke(1.dp, IndigoLight.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = IndigoLight,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "The Mechanics of the 20-20-20 Rule",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.5.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "1. Why 20 feet (~6 meters)?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = AmberGlow
                )
                Text(
                    text = "In optometry, light rays traveling 20 feet (6m) or more become parallel. When you look into the far distance, the ciliary muscles in your eyes completely relax from accommodation, halting digital eye strain and focal spasms.",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "2. Why 20 seconds?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = AmberGlow
                )
                Text(
                    text = "It takes about 20 seconds of continuous distant focus for ocular muscles to release accumulated tension and for reflexive blinking to redistribute your tear film across the dry cornea.",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "3. Why every 20 minutes?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = AmberGlow
                )
                Text(
                    text = "Normal blinking drops by over 66% during screen concentration (from 18 times/min down to 5 times/min). Intervening every 20 minutes prevents dry eyes before chronic inflammation starts.",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section 2: Circadian Bedtime Hygiene
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = TwilightDark),
            border = BorderStroke(1.dp, AmberGlow.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bedtime,
                        contentDescription = null,
                        tint = AmberGlow,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Circadian Rhythm & Blue Light",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.5.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "• The Pineal Gland & Melatonin",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = EyeSage
                )
                Text(
                    text = "Intrinsically photosensitive retinal ganglion cells (ipRGCs) are highly sensitive to blue light (460–480nm) emitted by OLED screens. Evening exposure suppresses melatonin production, delaying rapid eye movement (REM) and deep restorative sleep by up to 90 minutes.",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "• 90-Minute Sleep Architecture",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = EyeSage
                )
                Text(
                    text = "Human sleep cycles through Non-REM (Light & Slow-Wave Deep Sleep) and REM sleep every 90 minutes. Setting a wake time that is a multiple of 90 minutes prevents waking during deep Stage 3/4 sleep, eliminating morning grogginess (sleep inertia).",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
