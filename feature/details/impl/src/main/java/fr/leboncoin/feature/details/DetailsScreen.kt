package fr.leboncoin.feature.details

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
import fr.leboncoin.feature.details.impl.R
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private const val ALBUM_CONTENT_TYPE = "album"

@Composable
internal fun DetailsScreen(
    albumId: Int,
    modifier: Modifier = Modifier,
    detailsViewModel: DetailsViewModel = koinViewModel { parametersOf(albumId) },
) {
    val uiState by detailsViewModel.uiState.collectAsStateWithLifecycle()
    TrackScreenViewEvent("DetailsScreen")

    val snackbarHostState = LocalAppSnackbarHostState.current
    val favoriteFailedMessage = stringResource(R.string.feature_details_impl_favorite_failed)
    LaunchedEffect(detailsViewModel, snackbarHostState) {
        detailsViewModel.favoriteFailed.collect { snackbarHostState.show(favoriteFailedMessage) }
    }

    DetailsScreen(
        uiState = uiState,
        onFavoriteClick = { photo -> detailsViewModel.onFavoriteClick(photo.id) },
        modifier = modifier,
    )
}

@Composable
internal fun DetailsScreen(
    uiState: DetailsUiState,
    onFavoriteClick: (Album) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppScaffold(modifier = modifier) { contentPadding ->
        when (uiState) {
            DetailsUiState.Loading -> LoadingScreen(modifier = Modifier.padding(contentPadding))
            DetailsUiState.LoadFailed -> ErrorScreen(
                errorMessage = stringResource(R.string.feature_details_impl_load_failed),
                modifier = Modifier.padding(contentPadding),
            )

            DetailsUiState.Empty -> ErrorScreen(
                errorMessage = stringResource(R.string.feature_details_impl_empty),
                modifier = Modifier.padding(contentPadding),
            )

            is DetailsUiState.Success -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spaceMedium),
                contentPadding = contentPadding,
            ) {
                items(
                    items = uiState.photos,
                    key = { photo -> photo.id },
                    contentType = { ALBUM_CONTENT_TYPE },
                ) { photo ->
                    AlbumItem(album = photo, onFavoriteClick = onFavoriteClick)
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun DetailsScreenSuccessPreview() {
    AppTheme {
        AppSurface {
            DetailsScreen(
                uiState = DetailsUiState.Success(
                    listOf(
                        Album(id = 1, albumId = 1, title = "accusamus beatae ad facilis", url = "", thumbnailUrl = ""),
                        Album(
                            id = 2,
                            albumId = 1,
                            title = "reprehenderit est deserunt velit",
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
private fun DetailsScreenEmptyPreview() {
    AppTheme {
        AppSurface {
            DetailsScreen(uiState = DetailsUiState.Empty, onFavoriteClick = {})
        }
    }
}
