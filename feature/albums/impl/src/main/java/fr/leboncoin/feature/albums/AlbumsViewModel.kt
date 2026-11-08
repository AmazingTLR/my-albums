package fr.leboncoin.feature.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.leboncoin.core.analytics.AnalyticsLogger
import fr.leboncoin.core.model.AlbumGroup
import fr.leboncoin.core.usecase.GetAlbumGroupsUseCase
import fr.leboncoin.core.usecase.RefreshAlbumsUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

/**
 * Offline first: the stored groups are shown right away and refreshed from the network in the
 * background. The loading screen only appears on the very first launch, when nothing is stored yet.
 */
class AlbumsViewModel(
    private val getAlbumGroupsUseCase: GetAlbumGroupsUseCase,
    private val refreshAlbumsUseCase: RefreshAlbumsUseCase,
    private val analyticsLogger: AnalyticsLogger,
) : ViewModel() {

    private val refreshRequests = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    val uiState: StateFlow<AlbumsUiState> =
        flow {
            emit(AlbumsUiState.Loading)
            var groups = loadGroups()
            if (groups.isEmpty()) {
                // First launch: nothing to show, so the download has to succeed.
                if (refreshAlbumsUseCase() is RefreshAlbumsUseCase.Result.Failure) {
                    emit(AlbumsUiState.LoadFailed)
                    return@flow
                }
                groups = loadGroups()
                emit(AlbumsUiState.Success(groups))
            } else {
                emit(AlbumsUiState.Success(groups))
                groups = refreshInBackground(groups)
            }
            // Pull-to-refresh requests are handled sequentially: concurrent requests are conflated.
            refreshRequests.collect { groups = refreshInBackground(groups) }
        }
            .catch { emit(AlbumsUiState.LoadFailed) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AlbumsUiState.Loading,
            )

    fun onRefresh() {
        refreshRequests.tryEmit(Unit)
    }

    fun onAlbumSelected(albumId: Int) {
        analyticsLogger.logEventAlbumSelected(albumId = albumId)
    }

    private suspend fun loadGroups(): List<AlbumGroup> =
        when (val result = getAlbumGroupsUseCase()) {
            GetAlbumGroupsUseCase.Result.Empty -> emptyList()
            is GetAlbumGroupsUseCase.Result.Success -> result.groups
        }

    /**
     * Keeps [current] on screen while downloading. A failed refresh is not an error as long as
     * stored data is displayed.
     */
    private suspend fun FlowCollector<AlbumsUiState>.refreshInBackground(
        current: List<AlbumGroup>,
    ): List<AlbumGroup> {
        emit(AlbumsUiState.Success(current, isRefreshing = true))
        val updated = when (refreshAlbumsUseCase()) {
            RefreshAlbumsUseCase.Result.Success -> loadGroups()
            RefreshAlbumsUseCase.Result.Failure -> current
        }
        emit(AlbumsUiState.Success(updated, isRefreshing = false))
        return updated
    }
}
