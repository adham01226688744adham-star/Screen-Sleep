package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = IndigoLight,
    onPrimary = Color.White,
    primaryContainer = IndigoContainer,
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = AmberWarm,
    onSecondary = Color(0xFF78350F),
    secondaryContainer = Color(0xFF451A03),
    onSecondaryContainer = AmberGlow,
    tertiary = EyeSage,
    onTertiary = Color.White,
    tertiaryContainer = EyeSageContainer,
    onTertiaryContainer = Color(0xFFA7F3D0),
    background = MidnightBlue,
    onBackground = SoftWhite,
    surface = TwilightDark,
    onSurface = SoftWhite,
    surfaceVariant = SlateNavy,
    onSurfaceVariant = TextMuted,
    outline = Color(0xFF334155),
    outlineVariant = Color(0xFF1E293B)
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEEF2FF),
    onPrimaryContainer = Color(0xFF312E81),
    secondary = LightSecondary,
    onSecondary = Color.White,
    secondaryContainer = AmberLight,
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = Color(0xFF059669),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD1FAE5),
    onTertiaryContainer = Color(0xFF065F46),
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF475569),
    outline = LightOutline,
    outlineVariant = Color(0xFFE2E8F0)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to soothing dark mode for eye strain & bedtime app
    dynamicColor: Boolean = false, // Keep intentional circadian night palette
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
