package fr.leboncoin.androidrecruitmenttestapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import fr.leboncoin.core.navigation.Navigator
import fr.leboncoin.core.navigation.rememberNavigationState
import fr.leboncoin.core.navigation.toEntries
import fr.leboncoin.feature.albums.AlbumsNavKey
import fr.leboncoin.feature.albums.navigation.albumsEntry
import fr.leboncoin.feature.details.navigation.detailsEntry

private val TopLevelKeys: Set<NavKey> = setOf(AlbumsNavKey)

@Composable
fun PhotoNavigation() {
    val navigationState = rememberNavigationState(
        startKey = AlbumsNavKey,
        topLevelKeys = TopLevelKeys,
    )
    val navigator = remember(navigationState) { Navigator(navigationState) }

    val entryProvider = entryProvider {
        albumsEntry(navigator)
        detailsEntry()
    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = navigator::goBack,
    )
}
