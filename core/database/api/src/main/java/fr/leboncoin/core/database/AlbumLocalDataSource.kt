package fr.leboncoin.core.database

import fr.leboncoin.core.model.Album
import fr.leboncoin.core.model.AlbumGroup

interface AlbumLocalDataSource {
    suspend fun replaceAll(albums: List<Album>)

    suspend fun getAlbumGroups(numberOfInitialCover: Int): List<AlbumGroup>

    suspend fun getAlbumsById(albumId: Int): List<Album>
}
