package fr.leboncoin.feature.albums

import fr.leboncoin.core.model.AlbumGroup

sealed interface AlbumsUiState {
    data object Loading : AlbumsUiState
    data class Success(val groups: List<AlbumGroup>, val isRefreshing: Boolean = false) : AlbumsUiState
    data object LoadFailed : AlbumsUiState
}
