package fr.leboncoin.feature.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.leboncoin.core.common.exception.AppException
import fr.leboncoin.core.usecase.GetAlbumPhotosUseCase
import fr.leboncoin.core.usecase.ToggleAlbumFavoriteUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailsViewModel(
    albumId: Int,
    getAlbumPhotosUseCase: GetAlbumPhotosUseCase,
    private val toggleAlbumFavoriteUseCase: ToggleAlbumFavoriteUseCase,
) : ViewModel() {

    val uiState: StateFlow<DetailsUiState> =
        getAlbumPhotosUseCase(albumId)
            .map<GetAlbumPhotosUseCase.Result, DetailsUiState> { result ->
                when (result) {
                    GetAlbumPhotosUseCase.Result.Empty -> DetailsUiState.Empty
                    is GetAlbumPhotosUseCase.Result.Success -> DetailsUiState.Success(result.photos)
                }
            }
            .catch { emit(DetailsUiState.LoadFailed) }
            .onStart { emit(DetailsUiState.Loading) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DetailsUiState.Loading,
            )

    private val favoriteFailures = Channel<Unit>(Channel.BUFFERED)

    /** Emits each time a favorite could not be saved, so the screen can tell the user. */
    val favoriteFailed: Flow<Unit> = favoriteFailures.receiveAsFlow()

    fun onFavoriteClick(photoId: Int) {
        viewModelScope.launch {
            try {
                toggleAlbumFavoriteUseCase(photoId)
            } catch (_: AppException) {
                favoriteFailures.send(Unit)
            }
        }
    }
}
