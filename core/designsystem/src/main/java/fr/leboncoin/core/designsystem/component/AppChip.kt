package fr.leboncoin.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adevinta.spark.components.chips.ChipTinted

@Composable
fun AppChip(
    text: String,
    modifier: Modifier = Modifier,
) {
    ChipTinted(text = text, modifier = modifier)
}
