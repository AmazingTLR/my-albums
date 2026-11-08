package fr.leboncoin.core.repository

import fr.leboncoin.core.model.Album
import fr.leboncoin.core.model.AlbumGroup
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    /**
     * Reads the album groups from local storage only, without touching the network.
     * Empty until the first successful [refreshAlbums].
     * @throws fr.leboncoin.core.common.exception.AppException when the storage cannot be read.
     */
    suspend fun getAlbumGroups(numberOfInitialCover: Int): List<AlbumGroup>

    /**
     * Downloads the albums and replaces the stored ones. Stored data is left untouched on failure.
     * @throws fr.leboncoin.core.common.exception.AppException when the download or the storage fails.
     */
    suspend fun refreshAlbums()

    // Observe the photos of a single album from local storage
    fun observeAlbumPhotos(albumId: Int): Flow<List<Album>>

    fun observeFavoriteAlbums(): Flow<List<Album>>

    suspend fun toggleAlbumFavorite(albumId: Int)
}
