package fr.leboncoin.feature.favorites

import fr.leboncoin.core.model.Album

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState
    data class Success(val albums: List<Album>) : FavoritesUiState
    data object Empty : FavoritesUiState
    data object LoadFailed : FavoritesUiState
}
