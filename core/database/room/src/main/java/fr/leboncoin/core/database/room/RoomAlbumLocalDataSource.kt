package fr.leboncoin.core.database.room

import android.database.SQLException
import androidx.room.withTransaction
import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.database.AlbumLocalDataSource
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.model.AlbumGroup
import kotlin.coroutines.cancellation.CancellationException

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
}

/**
 * Translates storage failures into [DataUnavailableException].
 * [CancellationException] is always rethrown so structured concurrency keeps working.
 */
private suspend fun <T> safeDbCall(block: suspend () -> T): T =
    try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: SQLException) {
        throw DataUnavailableException(cause = e)
    } catch (e: IllegalStateException) {
        throw DataUnavailableException(cause = e)
    }
