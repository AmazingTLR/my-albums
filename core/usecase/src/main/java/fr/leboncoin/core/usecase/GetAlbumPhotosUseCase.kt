package fr.leboncoin.core.usecase

import fr.leboncoin.core.model.Album
import fr.leboncoin.core.repository.AlbumRepository

class GetAlbumPhotosUseCase(
    private val albumRepository: AlbumRepository,
) {
    sealed interface Result {
        data class Success(val photos: List<Album>) : Result
        data object Empty : Result
    }

    /**
     * @throws fr.leboncoin.core.common.exception.AppException when the photos cannot be retrieved.
     */
    suspend operator fun invoke(albumId: Int): Result {
        val photos = albumRepository.getAlbumPhotos(albumId)
        return if (photos.isEmpty()) Result.Empty else Result.Success(photos)
    }
}
