package fr.leboncoin.core.repository

import fr.leboncoin.core.model.Album
import fr.leboncoin.core.model.AlbumGroup
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    /**
     * Fetches the albums from the network.
     * Return stored albums if network fails
     * @throws fr.leboncoin.core.common.exception.AppException when there is neither network nor stored data.
     */
    suspend fun getAlbumGroups(numberOfInitialCover: Int): List<AlbumGroup>

    // Observe the photos of a single album from local storage
    fun observeAlbumPhotos(albumId: Int): Flow<List<Album>>

    fun observeFavoriteAlbums(): Flow<List<Album>>

    suspend fun toggleAlbumFavorite(albumId: Int)
}
