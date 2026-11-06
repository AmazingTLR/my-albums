package fr.leboncoin.core.designsystem.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import fr.leboncoin.core.designsystem.component.AppSurface
import fr.leboncoin.core.designsystem.component.AppText
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.designsystem.theme.ThemePreviews

@Composable
fun ErrorScreen(
    errorMessage: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(AppTheme.dimensions.spaceMedium),
        contentAlignment = Alignment.Center,
    ) {
        AppText(
            text = errorMessage,
            style = AppTheme.typography.body,
            textAlign = TextAlign.Center,
        )
    }
}

@ThemePreviews
@Composable
private fun ErrorScreenPreview() {
    AppTheme {
        AppSurface {
            ErrorScreen(errorMessage = "Failed to load albums")
        }
    }
}
