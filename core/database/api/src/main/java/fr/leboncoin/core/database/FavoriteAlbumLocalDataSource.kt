package fr.leboncoin.core.database

import fr.leboncoin.core.model.Album
import kotlinx.coroutines.flow.Flow

interface FavoriteAlbumLocalDataSource {
    suspend fun toggle(albumId: Int)

    fun observeFavoriteIds(): Flow<Set<Int>>

    fun observeFavoriteAlbums(): Flow<List<Album>>
}
