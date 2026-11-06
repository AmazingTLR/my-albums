package fr.leboncoin.core.usecase

import fr.leboncoin.core.model.Album
import fr.leboncoin.core.repository.AlbumRepository

class GetAlbumsUseCase(
    private val albumRepository: AlbumRepository,
) {
    sealed interface Result {
        data class Success(val albums: List<Album>) : Result
        data object Empty : Result
    }

    /**
     * @throws fr.leboncoin.core.common.exception.AppException when the albums cannot be retrieved.
     */
    suspend operator fun invoke(): Result {
        val albums = albumRepository.getAlbums()
        return if (albums.isEmpty()) Result.Empty else Result.Success(albums)
    }
}
