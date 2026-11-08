package fr.leboncoin.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adevinta.spark.components.toggles.RadioButtonLabelled

@Composable
fun AppRadioButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    RadioButtonLabelled(selected = selected, onClick = onClick, modifier = modifier) {
        AppText(text = text)
    }
}
