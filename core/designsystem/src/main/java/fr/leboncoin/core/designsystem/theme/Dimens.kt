package fr.leboncoin.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppDimensions(
    val spaceXXS: Dp = 2.dp,
    val spaceXS: Dp = 4.dp,
    val spaceSmall: Dp = 8.dp,
    val spaceMediumSmall: Dp = 14.dp,
    val spaceMedium: Dp = 16.dp,
    val spaceLarge: Dp = 24.dp,
    val spaceXLarge: Dp = 32.dp,
    val albumItemHeight: Dp = 120.dp,
)

internal val LocalDimensions = staticCompositionLocalOf { AppDimensions() }
