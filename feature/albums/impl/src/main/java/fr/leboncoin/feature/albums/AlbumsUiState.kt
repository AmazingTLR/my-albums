package fr.leboncoin.feature.albums

import fr.leboncoin.core.model.Album

sealed interface AlbumsUiState {
    data object Loading : AlbumsUiState
    data class Success(val albums: List<Album>) : AlbumsUiState
    data object LoadFailed : AlbumsUiState
}
