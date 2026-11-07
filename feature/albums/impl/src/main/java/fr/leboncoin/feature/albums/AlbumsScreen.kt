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
import fr.leboncoin.core.model.AlbumGroup
import fr.leboncoin.core.ui.AlbumGroupItem
import fr.leboncoin.core.ui.analytics.TrackScreenViewEvent
import fr.leboncoin.feature.albums.impl.R
import org.koin.androidx.compose.koinViewModel

private const val ALBUM_GROUP_CONTENT_TYPE = "album_group"

@Composable
internal fun AlbumsScreen(
    onAlbumClick: (albumId: Int) -> Unit,
    modifier: Modifier = Modifier,
    albumsViewModel: AlbumsViewModel = koinViewModel(),
) {
    val uiState by albumsViewModel.uiState.collectAsStateWithLifecycle()
    TrackScreenViewEvent("AlbumsScreen")
    AlbumsScreen(
        uiState = uiState,
        onAlbumClick = { albumId ->
            albumsViewModel.onAlbumSelected(albumId)
            onAlbumClick(albumId)
        },
        modifier = modifier,
    )
}

@Composable
internal fun AlbumsScreen(
    uiState: AlbumsUiState,
    onAlbumClick: (albumId: Int) -> Unit,
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
                items(
                    items = uiState.groups,
                    key = { group -> group.albumId },
                    contentType = { ALBUM_GROUP_CONTENT_TYPE },
                ) { group ->
                    AlbumGroupItem(group = group, onItemSelected = onAlbumClick)
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
                        AlbumGroup(albumId = 1, photoCount = 50, coverUrls = listOf("", "", "", "")),
                        AlbumGroup(albumId = 2, photoCount = 1, coverUrls = listOf("")),
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
