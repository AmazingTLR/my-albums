package fr.leboncoin.core.repository

import fr.leboncoin.core.model.Album
import fr.leboncoin.core.model.AlbumGroup

interface AlbumRepository {
    /**
     * Fetches the albums from the network.
     * Return stored albums if network fails
     * @throws fr.leboncoin.core.common.exception.AppException when there is neither network nor stored data.
     */
    suspend fun getAlbumGroups(numberOfInitialCover: Int): List<AlbumGroup>

    // Returns the photos of a single album from local storage
    suspend fun getAlbumPhotos(albumId: Int): List<Album>
}
