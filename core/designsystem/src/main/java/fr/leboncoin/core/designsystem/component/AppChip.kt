package fr.leboncoin.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adevinta.spark.components.chips.ChipTinted

/**
 * Read-only label.
 *
 * Spark Chips consume taps even without a click handler.
 * Inside a clickable card they prevent the card from being selected.
 * Disabling isn't an option as it will look disabled.
 *
 * Hack for the test: Forward the tap to the parent card when there is one, otherwise do nothing.
 * In a real-world scenario: I would escalate this with the appropriate team.
 */
@Composable
fun AppChip(
    text: String,
    modifier: Modifier = Modifier,
) {
    ChipTinted(text = text, modifier = modifier, onClick = LocalAppCardOnClick.current ?: {})
}
