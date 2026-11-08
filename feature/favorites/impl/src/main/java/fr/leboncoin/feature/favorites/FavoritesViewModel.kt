package fr.leboncoin.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.leboncoin.core.common.exception.AppException
import fr.leboncoin.core.usecase.ObserveFavoriteAlbumsUseCase
import fr.leboncoin.core.usecase.ToggleAlbumFavoriteUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    observeFavoriteAlbumsUseCase: ObserveFavoriteAlbumsUseCase,
    private val toggleAlbumFavoriteUseCase: ToggleAlbumFavoriteUseCase,
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> =
        observeFavoriteAlbumsUseCase()
            .map<ObserveFavoriteAlbumsUseCase.Result, FavoritesUiState> { result ->
                when (result) {
                    ObserveFavoriteAlbumsUseCase.Result.Empty -> FavoritesUiState.Empty
                    is ObserveFavoriteAlbumsUseCase.Result.Success -> FavoritesUiState.Success(result.albums)
                }
            }
            .catch { emit(FavoritesUiState.LoadFailed) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FavoritesUiState.Loading,
            )

    private val favoriteFailures = Channel<Unit>(Channel.BUFFERED)

    /** Emits each time a favorite could not be saved, so the screen can tell the user. */
    val favoriteFailed: Flow<Unit> = favoriteFailures.receiveAsFlow()

    fun onFavoriteClick(albumId: Int) {
        viewModelScope.launch {
            try {
                toggleAlbumFavoriteUseCase(albumId)
            } catch (_: AppException) {
                favoriteFailures.send(Unit)
            }
        }
    }
}
