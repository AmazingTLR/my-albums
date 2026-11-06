package fr.leboncoin.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adevinta.spark.components.surface.Surface

/** Background container, mainly used to wrap previews. */
@Composable
fun AppSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(modifier = modifier, content = content)
}
