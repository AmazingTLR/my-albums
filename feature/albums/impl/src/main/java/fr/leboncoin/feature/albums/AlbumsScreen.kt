package fr.leboncoin.feature.albums

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.leboncoin.core.designsystem.component.AppScaffold
import fr.leboncoin.core.designsystem.screen.ErrorScreen
import fr.leboncoin.core.designsystem.screen.LoadingScreen
import fr.leboncoin.core.designsystem.component.AppSurface
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.designsystem.theme.ThemePreviews
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.ui.AlbumItem
import fr.leboncoin.core.ui.analytics.TrackScreenViewEvent
import fr.leboncoin.feature.albums.impl.R
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun AlbumsScreen(
    onAlbumClick: (Album) -> Unit,
    modifier: Modifier = Modifier,
    albumsViewModel: AlbumsViewModel = koinViewModel(),
) {
    val uiState by albumsViewModel.uiState.collectAsStateWithLifecycle()
    TrackScreenViewEvent("AlbumsScreen")
    AlbumsScreen(
        uiState = uiState,
        onAlbumClick = { album ->
            albumsViewModel.onAlbumSelected(album)
            onAlbumClick(album)
        },
        modifier = modifier,
    )
}

@Composable
internal fun AlbumsScreen(
    uiState: AlbumsUiState,
    onAlbumClick: (Album) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppScaffold(modifier = modifier) { contentPadding ->
        when (uiState) {
            AlbumsUiState.Loading -> LoadingScreen(modifier = Modifier.padding(contentPadding))
            AlbumsUiState.LoadFailed -> ErrorScreen(
                errorMessage = stringResource(R.string.feature_albums_impl_load_failed),
                modifier = Modifier.padding(contentPadding),
            )

            is AlbumsUiState.Success -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spaceMedium),
                contentPadding = contentPadding,
            ) {
                items(items = uiState.albums, key = { album -> album.id }) { album ->
                    AlbumItem(album = album, onItemSelected = onAlbumClick)
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun AlbumsScreenSuccessPreview() {
    AppTheme {
        AppSurface {
            AlbumsScreen(
                uiState = AlbumsUiState.Success(
                    listOf(
                        Album(1, 1, "accusamus beatae ad facilis cum similique qui sunt", "", ""),
                        Album(2, 1, "reprehenderit est deserunt velit ipsam", "", ""),
                    ),
                ),
                onAlbumClick = {},
            )
        }
    }
}

@ThemePreviews
@Composable
private fun AlbumsScreenLoadFailedPreview() {
    AppTheme {
        AppSurface {
            AlbumsScreen(uiState = AlbumsUiState.LoadFailed, onAlbumClick = {})
        }
    }
}
