package fr.leboncoin.core.repository

import fr.leboncoin.core.common.exception.AppException
import fr.leboncoin.core.database.AlbumLocalDataSource
import fr.leboncoin.core.database.FavoriteAlbumLocalDataSource
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.model.AlbumGroup
import fr.leboncoin.core.network.NetworkDataSource
import fr.leboncoin.core.network.dto.AlbumDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

/**
 * Network first, local data source as fallback: every fetch replaces the stored data
 * All read are done by the local data source.
 */
internal class AlbumRepositoryImpl(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: AlbumLocalDataSource,
    private val favoriteDataSource: FavoriteAlbumLocalDataSource,
    private val dispatcher: CoroutineDispatcher,
) : AlbumRepository {

    // Prevents concurrent callers from downloading and storing the same payload twice.
    private val fetchMutex = Mutex()

    override suspend fun getAlbumGroups(numberOfInitialCover: Int): List<AlbumGroup> = withContext(dispatcher) {
        val fetchFailure = fetch()
        localDataSource.getAlbumGroups(numberOfInitialCover).also {
            if (it.isEmpty() && fetchFailure != null) throw fetchFailure
        }
    }

    override fun observeAlbumPhotos(albumId: Int): Flow<List<Album>> = flow {
        if (localDataSource.getAlbumsById(albumId).isEmpty()) {
            val fetchFailure = fetch()
            if (fetchFailure != null && localDataSource.getAlbumsById(albumId).isEmpty()) throw fetchFailure
        }
        emitAll(
            combine(
                localDataSource.observeAlbumsById(albumId),
                favoriteDataSource.observeFavoriteIds(),
            ) { photos, favoriteIds ->
                photos.map { photo -> photo.copy(isFavorite = photo.id in favoriteIds) }
            },
        )
    }.flowOn(dispatcher)

    override fun observeFavoriteAlbums(): Flow<List<Album>> =
        favoriteDataSource.observeFavoriteAlbums().flowOn(dispatcher)

    override suspend fun toggleAlbumFavorite(albumId: Int) = withContext(dispatcher) {
        favoriteDataSource.toggle(albumId)
    }

    private suspend fun fetch(): AppException? = fetchMutex.withLock {
        try {
            val albums = networkDataSource.getAlbums().map { it.toAlbum() }
            localDataSource.replaceAll(albums)
            null
        } catch (e: CancellationException) {
            throw e
        } catch (e: AppException) {
            e
        }
    }
}

private fun AlbumDto.toAlbum() = Album(
    id = id,
    albumId = albumId,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
)
