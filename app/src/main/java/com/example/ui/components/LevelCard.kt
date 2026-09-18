package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
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
import com.example.data.model.ManagementLevel
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.EyeSage
import com.example.ui.theme.IndigoLight
import com.example.ui.theme.TwilightDark

@Composable
fun LevelCard(
    level: ManagementLevel,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = when {
        isSelected && level.levelNumber == 2 -> AmberGlow
        isSelected -> IndigoLight
        else -> Color.White.copy(alpha = 0.08f)
    }

    val containerColor = if (isSelected) {
        TwilightDark.copy(alpha = 0.95f)
    } else {
        TwilightDark.copy(alpha = 0.5f)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .testTag("level_card_${level.levelNumber}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = if (isSelected) "Selected" else "Unselected",
                        tint = if (isSelected) {
                            if (level.levelNumber == 2) AmberGlow else IndigoLight
                        } else Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = level.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when (level.levelNumber) {
                        2 -> AmberGlow.copy(alpha = 0.2f)
                        else -> IndigoLight.copy(alpha = 0.15f)
                    }
                ) {
                    Text(
                        text = level.badge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (level.levelNumber == 2) AmberGlow else IndigoLight,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = level.subtitle,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = level.ruleSummary,
                fontSize = 12.5.sp,
                color = EyeSage,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = level.detailedGuidance,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = Color.White.copy(alpha = 0.65f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.06f)
                ) {
                    Text(
                        text = "Interval: ${level.intervalMinutes} mins",
                        fontSize = 11.5.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White.copy(alpha = 0.06f)
                ) {
                    Text(
                        text = "Rest: ${level.breakSeconds} secs (${level.distanceDescription})",
                        fontSize = 11.5.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
