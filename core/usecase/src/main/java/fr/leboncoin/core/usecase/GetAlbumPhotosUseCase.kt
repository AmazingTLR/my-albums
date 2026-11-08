package fr.leboncoin.core.usecase

import fr.leboncoin.core.model.Album
import fr.leboncoin.core.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
    operator fun invoke(albumId: Int): Flow<Result> =
        albumRepository.observeAlbumPhotos(albumId)
            .map { photos -> if (photos.isEmpty()) Result.Empty else Result.Success(photos) }
}
