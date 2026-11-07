package fr.leboncoin.feature.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.leboncoin.core.analytics.AnalyticsLogger
import fr.leboncoin.core.usecase.GetAlbumGroupsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class AlbumsViewModel(
    getAlbumGroupsUseCase: GetAlbumGroupsUseCase,
    private val analyticsLogger: AnalyticsLogger,
) : ViewModel() {

    val uiState: StateFlow<AlbumsUiState> =
        flow { emit(getAlbumGroupsUseCase()) }
            .map<GetAlbumGroupsUseCase.Result, AlbumsUiState> { result ->
                when (result) {
                    GetAlbumGroupsUseCase.Result.Empty -> AlbumsUiState.Success(emptyList())
                    is GetAlbumGroupsUseCase.Result.Success -> AlbumsUiState.Success(result.groups)
                }
            }
            .catch { emit(AlbumsUiState.LoadFailed) }
            .onStart { emit(AlbumsUiState.Loading) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AlbumsUiState.Loading,
            )

    fun onAlbumSelected(albumId: Int) {
        analyticsLogger.logEventAlbumSelected(albumId = albumId)
    }
}
