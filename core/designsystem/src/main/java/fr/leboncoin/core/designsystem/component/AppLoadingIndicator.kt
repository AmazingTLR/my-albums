package fr.leboncoin.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adevinta.spark.components.progress.Spinner

@Composable
fun AppLoadingIndicator(modifier: Modifier = Modifier) {
    Spinner(modifier = modifier)
}
