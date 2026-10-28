package com.mx.weartwo.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.material3.MaterialTheme

private val PulsoColorScheme = ColorScheme(
    primary = Color(0xFFFFB300),
    primaryDim = Color(0xFFB28704),
    primaryContainer = Color(0xFF6B4F00),
    onPrimary = Color(0xFF221B00),
    onPrimaryContainer = Color(0xFFFFE08A),
    secondary = Color(0xFFFF6D4D),
    secondaryDim = Color(0xFFB24A30),
    secondaryContainer = Color(0xFF5A1E0C),
    onSecondary = Color(0xFF2B0D03),
    onSecondaryContainer = Color(0xFFFFB59D),
    tertiary = Color(0xFF80CBC4),
    tertiaryDim = Color(0xFF5AA39C),
    tertiaryContainer = Color(0xFF00534B),
    onTertiary = Color(0xFF00201C),
    onTertiaryContainer = Color(0xFFA7F1E9),
    surfaceContainerLow = Color(0xFF1A1813),
    surfaceContainer = Color(0xFF201D17),
    surfaceContainerHigh = Color(0xFF2A271F),
    onSurface = Color(0xFFEDE7DA),
    onSurfaceVariant = Color(0xFFCFC5B5),
    outline = Color(0xFF998F80),
    outlineVariant = Color(0xFF4C4639),
    background = Color(0xFF12100C),
    onBackground = Color(0xFFEDE7DA),
    error = Color(0xFFFFB4AB),
    errorDim = Color(0xFF93000A),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
)

@Composable
fun PulsoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PulsoColorScheme,
        content = content,
    )
}
