package fr.leboncoin.core.usecase

import fr.leboncoin.core.repository.AlbumRepository

class ToggleAlbumFavoriteUseCase(
    private val albumRepository: AlbumRepository,
) {
    /**
     * @throws fr.leboncoin.core.common.exception.AppException when the favorite cannot be stored.
     */
    suspend operator fun invoke(albumId: Int) = albumRepository.toggleAlbumFavorite(albumId)
}
