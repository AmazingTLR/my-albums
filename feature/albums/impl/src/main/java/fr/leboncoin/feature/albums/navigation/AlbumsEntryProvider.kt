package fr.leboncoin.feature.albums.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import fr.leboncoin.core.navigation.Navigator
import fr.leboncoin.feature.albums.AlbumsNavKey
import fr.leboncoin.feature.albums.AlbumsScreen
import fr.leboncoin.feature.details.navigateToDetails

fun EntryProviderScope<NavKey>.albumsEntry(navigator: Navigator) {
    entry<AlbumsNavKey> {
        AlbumsScreen(
            onAlbumClick = { albumId -> navigator.navigateToDetails(albumId) },
        )
    }
}
