package fr.leboncoin.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

@Immutable
data class AppTypography(
    val body: TextStyle,
    val caption: TextStyle,
)
