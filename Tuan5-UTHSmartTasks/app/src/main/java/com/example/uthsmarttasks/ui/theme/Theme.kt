package com.example.uthsmarttasks.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BluePrimary = Color(0xFF2563EB)
val BlueVariant = Color(0xFF3B82F6)
val BlueBg = Color(0xFF1E40AF)
val BackgroundLight = Color(0xFFF8FAFC)
val SurfaceLight = Color(0xFFFFFFFF)

val CategoryStudyBg = Color(0xFFEFF6FF)
val CategoryStudyText = Color(0xFF2563EB)

val CategoryLifeBg = Color(0xFFFFF7ED)
val CategoryLifeText = Color(0xFFEA580C)

val PriorityHighBg = Color(0xFFFEE2E2)
val PriorityHighText = Color(0xFFDC2626)

val PriorityMediumBg = Color(0xFFFEF3C7)
val PriorityMediumText = Color(0xFFD97706)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = Color(0xFF1E40AF),
    secondary = Color(0xFF475569),
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF60A5FA),
    onPrimary = Color.Black,
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun UTHSmartTasksTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
