package fr.leboncoin.feature.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.leboncoin.core.designsystem.component.AppScaffold
import fr.leboncoin.core.designsystem.component.AppSurface
import fr.leboncoin.core.designsystem.screen.ErrorScreen
import fr.leboncoin.core.designsystem.screen.LoadingScreen
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.designsystem.theme.ThemePreviews
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.ui.AlbumItem
import fr.leboncoin.core.ui.LocalAppSnackbarHostState
import fr.leboncoin.core.ui.analytics.TrackScreenViewEvent
import fr.leboncoin.feature.favorites.impl.R
import org.koin.androidx.compose.koinViewModel

private const val ALBUM_CONTENT_TYPE = "album"

@Composable
internal fun FavoritesScreen(
    modifier: Modifier = Modifier,
    favoritesViewModel: FavoritesViewModel = koinViewModel(),
) {
    val uiState by favoritesViewModel.uiState.collectAsStateWithLifecycle()
    TrackScreenViewEvent("FavoritesScreen")

    val snackbarHostState = LocalAppSnackbarHostState.current
    val favoriteFailedMessage = stringResource(R.string.feature_favorites_impl_favorite_failed)
    LaunchedEffect(favoritesViewModel, snackbarHostState) {
        favoritesViewModel.favoriteFailed.collect { snackbarHostState.show(favoriteFailedMessage) }
    }

    FavoritesScreen(
        uiState = uiState,
        onFavoriteClick = { album -> favoritesViewModel.onFavoriteClick(album.id) },
        modifier = modifier,
    )
}

@Composable
internal fun FavoritesScreen(
    uiState: FavoritesUiState,
    onFavoriteClick: (Album) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppScaffold(modifier = modifier) { contentPadding ->
        when (uiState) {
            FavoritesUiState.Loading -> LoadingScreen(modifier = Modifier.padding(contentPadding))
            FavoritesUiState.LoadFailed -> ErrorScreen(
                errorMessage = stringResource(R.string.feature_favorites_impl_load_failed),
                modifier = Modifier.padding(contentPadding),
            )

            FavoritesUiState.Empty -> ErrorScreen(
                errorMessage = stringResource(R.string.feature_favorites_impl_empty),
                modifier = Modifier.padding(contentPadding),
            )

            is FavoritesUiState.Success -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spaceMedium),
                contentPadding = contentPadding,
            ) {
                items(
                    items = uiState.albums,
                    key = { album -> album.id },
                    contentType = { ALBUM_CONTENT_TYPE },
                ) { album ->
                    AlbumItem(album = album, onFavoriteClick = onFavoriteClick)
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun FavoritesScreenSuccessPreview() {
    AppTheme {
        AppSurface {
            FavoritesScreen(
                uiState = FavoritesUiState.Success(
                    listOf(
                        Album(
                            id = 1,
                            albumId = 1,
                            title = "accusamus beatae ad facilis",
                            url = "",
                            thumbnailUrl = "",
                            isFavorite = true,
                        ),
                    ),
                ),
                onFavoriteClick = {},
            )
        }
    }
}

@ThemePreviews
@Composable
private fun FavoritesScreenEmptyPreview() {
    AppTheme {
        AppSurface {
            FavoritesScreen(uiState = FavoritesUiState.Empty, onFavoriteClick = {})
        }
    }
}
