package fr.leboncoin.feature.details

import fr.leboncoin.core.model.Album

sealed interface DetailsUiState {
    data object Loading : DetailsUiState
    data class Success(val photos: List<Album>) : DetailsUiState
    data object Empty : DetailsUiState
    data object LoadFailed : DetailsUiState
}
