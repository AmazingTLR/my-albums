package fr.leboncoin.core.usecase

import fr.leboncoin.core.model.Album
import fr.leboncoin.core.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveFavoriteAlbumsUseCase(
    private val albumRepository: AlbumRepository,
) {
    sealed interface Result {
        data class Success(val albums: List<Album>) : Result
        data object Empty : Result
    }

    operator fun invoke(): Flow<Result> =
        albumRepository.observeFavoriteAlbums()
            .map { albums -> if (albums.isEmpty()) Result.Empty else Result.Success(albums) }
}
