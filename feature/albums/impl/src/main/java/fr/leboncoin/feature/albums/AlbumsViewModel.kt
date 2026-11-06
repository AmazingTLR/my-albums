package fr.leboncoin.feature.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.leboncoin.core.analytics.AnalyticsLogger
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.usecase.GetAlbumsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class AlbumsViewModel(
    getAlbumsUseCase: GetAlbumsUseCase,
    private val analyticsLogger: AnalyticsLogger,
) : ViewModel() {

    val uiState: StateFlow<AlbumsUiState> =
        flow { emit(getAlbumsUseCase()) }
            .map<GetAlbumsUseCase.Result, AlbumsUiState> { result ->
                when (result) {
                    GetAlbumsUseCase.Result.Empty -> AlbumsUiState.Success(emptyList())
                    is GetAlbumsUseCase.Result.Success -> AlbumsUiState.Success(result.albums)
                }
            }
            .catch { emit(AlbumsUiState.LoadFailed) }
            .onStart { emit(AlbumsUiState.Loading) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AlbumsUiState.Loading,
            )

    fun onAlbumSelected(album: Album) {
        analyticsLogger.logEventAlbumSelected(itemId = album.id)
    }
}
