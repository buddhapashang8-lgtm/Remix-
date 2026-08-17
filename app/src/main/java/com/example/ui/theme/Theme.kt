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

private val DarkColorScheme =
  darkColorScheme(
    primary = StudioVioletPrimary,
    onPrimary = Color.White,
    primaryContainer = StudioVioletDark,
    onPrimaryContainer = Color.White,
    secondary = StudioCyan,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF0E3A4B),
    onSecondaryContainer = StudioCyanLight,
    tertiary = StudioAmber,
    onTertiary = Color.Black,
    background = StudioDarkBg,
    onBackground = StudioTextPrimary,
    surface = StudioCardBg,
    onSurface = StudioTextPrimary,
    surfaceVariant = StudioCardElevated,
    onSurfaceVariant = StudioTextSecondary,
    outline = StudioBorder,
    outlineVariant = StudioBorderLight
  )

private val LightColorScheme = DarkColorScheme // Always maintain premium dark video studio aesthetic

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(colorScheme = DarkColorScheme, typography = Typography, content = content)
}
