package fr.leboncoin.core.database.room

import androidx.room.withTransaction
import fr.leboncoin.core.database.AlbumLocalDataSource
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.model.AlbumGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class RoomAlbumLocalDataSource(
    private val database: AlbumDatabase,
) : AlbumLocalDataSource {

    private val dao = database.albumDao()

    override suspend fun replaceAll(albums: List<Album>) = safeDbCall {
        dao.replaceAll(albums.map { it.toEntity() })
    }

    override suspend fun getAlbumGroups(numberOfInitialCover: Int): List<AlbumGroup> = safeDbCall {
        database.withTransaction {
            val coversByAlbum = dao.getAlbumCovers(numberOfInitialCover).groupBy({ it.albumId }, { it.thumbnailUrl })
            dao.getAlbumCounts().map { row ->
                AlbumGroup(
                    albumId = row.albumId,
                    photoCount = row.photoCount,
                    coverUrls = coversByAlbum[row.albumId].orEmpty(),
                )
            }
        }
    }

    override suspend fun getAlbumsById(albumId: Int): List<Album> = safeDbCall {
        dao.getAlbumsById(albumId).map { it.toAlbum() }
    }

    override fun observeAlbumsById(albumId: Int): Flow<List<Album>> =
        dao.observeAlbumsById(albumId)
            .map { entities -> entities.map { it.toAlbum() } }
            .safeDbFlow()
}
