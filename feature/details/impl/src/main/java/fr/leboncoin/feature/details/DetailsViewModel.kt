package fr.leboncoin.feature.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.leboncoin.core.usecase.GetAlbumPhotosUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class DetailsViewModel(
    albumId: Int,
    getAlbumPhotosUseCase: GetAlbumPhotosUseCase,
) : ViewModel() {

    val uiState: StateFlow<DetailsUiState> =
        flow { emit(getAlbumPhotosUseCase(albumId)) }
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
}
