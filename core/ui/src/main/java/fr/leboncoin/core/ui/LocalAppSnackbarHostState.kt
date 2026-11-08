package fr.leboncoin.core.ui

import androidx.compose.runtime.staticCompositionLocalOf
import fr.leboncoin.core.designsystem.component.AppSnackbarHostState

val LocalAppSnackbarHostState = staticCompositionLocalOf<AppSnackbarHostState> {
    error("No AppSnackbarHostState provided: wrap the content in the app shell.")
}
