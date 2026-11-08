package fr.leboncoin.androidrecruitmenttestapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import fr.leboncoin.androidrecruitmenttestapp.R
import fr.leboncoin.core.designsystem.component.AppScaffold
import fr.leboncoin.core.designsystem.component.AppSnackbarHost
import fr.leboncoin.core.designsystem.component.AppSnackbarHostState
import fr.leboncoin.core.navigation.Navigator
import fr.leboncoin.core.navigation.rememberNavigationState
import fr.leboncoin.core.navigation.toEntries
import fr.leboncoin.core.ui.LocalAppSnackbarHostState
import fr.leboncoin.feature.albums.AlbumsNavKey
import fr.leboncoin.feature.albums.navigation.albumsEntry
import fr.leboncoin.feature.details.navigation.detailsEntry

private val TopLevelKeys: Set<NavKey> = setOf(AlbumsNavKey)

@Composable
fun PhotoNavigation(appState: PhotoAppState) {
    val navigationState = rememberNavigationState(
        startKey = AlbumsNavKey,
        topLevelKeys = TopLevelKeys,
    )
    val navigator = remember(navigationState) { Navigator(navigationState) }

    val snackbarHostState = remember { AppSnackbarHostState() }
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    // Resolved in composition, outside the effect.
    val notConnectedMessage = stringResource(R.string.not_connected)
    // The snackbar stays while offline and is removed when the effect is cancelled, i.e. back online.
    LaunchedEffect(isOffline) {
        if (isOffline) snackbarHostState.showIndefinitely(notConnectedMessage)
    }

    val entryProvider = entryProvider {
        albumsEntry(navigator)
        detailsEntry()
    }

    CompositionLocalProvider(LocalAppSnackbarHostState provides snackbarHostState) {
        AppScaffold(
            snackbarHost = { AppSnackbarHost(state = snackbarHostState) },
        ) { contentPadding ->
            // Screens own a Scaffold too: consume the insets here so they are not applied twice.
            Box(Modifier.padding(contentPadding).consumeWindowInsets(contentPadding)) {
                NavDisplay(
                    entries = navigationState.toEntries(entryProvider),
                    onBack = navigator::goBack,
                )
            }
        }
    }
}
