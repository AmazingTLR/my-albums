package fr.leboncoin.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.adevinta.spark.components.scaffold.Scaffold

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(modifier = modifier, snackbarHost = snackbarHost, content = content)
}
