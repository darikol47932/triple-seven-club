package com.Kiwdidi6.Dicrov.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ClubColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = BgBase,
    primaryContainer = SurfaceWarm,
    onPrimaryContainer = GoldLight,
    secondary = GoldLight,
    onSecondary = BgBase,
    secondaryContainer = Surface2,
    onSecondaryContainer = GoldLight,
    tertiary = Success,
    onTertiary = BgBase,
    background = BgBase,
    onBackground = TextPrimary,
    surface = Surface1,
    onSurface = TextPrimary,
    surfaceVariant = Surface2,
    onSurfaceVariant = TextSecondary,
    outline = GoldDim,
    outlineVariant = TextMuted,
    error = Danger,
    onError = BgBase
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ClubColorScheme,
        typography = AppTypography,
        content = content
    )
}
