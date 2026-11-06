package fr.leboncoin.core.repository

import fr.leboncoin.core.model.Album

interface AlbumRepository {
    /**
     * Fetches all albums.
     *
     * @throws fr.leboncoin.core.common.exception.AppException when the albums cannot be retrieved.
     */
    suspend fun getAlbums(): List<Album>
}
