package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LuxuryDarkColorScheme = darkColorScheme(
    primary = ScopeRed,
    onPrimary = Color.White,
    primaryContainer = ScopeRedContainer,
    onPrimaryContainer = ScopeRed,
    secondary = ScopeGold,
    onSecondary = Color.Black,
    secondaryContainer = ScopeGoldContainer,
    onSecondaryContainer = ScopeGoldLight,
    tertiary = StatusCyan,
    onTertiary = Color.Black,
    background = ObsidianBg,
    onBackground = TextWhite,
    surface = ObsidianSurface,
    onSurface = TextWhite,
    surfaceVariant = ObsidianSurfaceVariant,
    onSurfaceVariant = TextMuted,
    outline = ObsidianCardBorder,
    outlineVariant = ObsidianDivider
)

@Composable
fun ScopeDexTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LuxuryDarkColorScheme,
        typography = Typography,
        content = content
    )
}
