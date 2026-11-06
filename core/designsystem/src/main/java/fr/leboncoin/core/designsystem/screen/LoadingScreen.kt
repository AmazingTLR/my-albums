package fr.leboncoin.core.designsystem.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.leboncoin.core.designsystem.component.AppLoadingIndicator
import fr.leboncoin.core.designsystem.component.AppSurface
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.designsystem.theme.ThemePreviews

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AppLoadingIndicator()
    }
}

@ThemePreviews
@Composable
private fun LoadingScreenPreview() {
    AppTheme {
        AppSurface {
            LoadingScreen()
        }
    }
}
