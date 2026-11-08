package fr.leboncoin.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adevinta.spark.ExperimentalSparkApi
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonIcons
import com.adevinta.spark.components.iconbuttons.toggle.IconToggleButtonTinted

@OptIn(ExperimentalSparkApi::class)
@Composable
fun AppIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    checkedIcon: AppIcon,
    uncheckedIcon: AppIcon,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    IconToggleButtonTinted(
        checked = checked,
        onCheckedChange = onCheckedChange,
        icons = IconToggleButtonIcons(checked = checkedIcon.spark, unchecked = uncheckedIcon.spark),
        modifier = modifier,
        contentDescription = contentDescription,
    )
}
