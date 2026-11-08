package fr.leboncoin.core.database.room

import fr.leboncoin.core.database.FavoriteAlbumLocalDataSource
import fr.leboncoin.core.model.Album
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class RoomFavoriteAlbumLocalDataSource(
    private val dao: FavoriteAlbumDao,
) : FavoriteAlbumLocalDataSource {

    override suspend fun toggle(albumId: Int) = safeDbCall { dao.toggle(albumId) }

    override fun observeFavoriteIds(): Flow<Set<Int>> =
        dao.observeFavoriteIds().map { it.toSet() }.safeDbFlow()

    override fun observeFavoriteAlbums(): Flow<List<Album>> =
        dao.observeFavoriteAlbums()
            .map { entities -> entities.map { it.toAlbum().copy(isFavorite = true) } }
            .safeDbFlow()
}
