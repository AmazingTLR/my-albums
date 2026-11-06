package fr.leboncoin.core.repository

import fr.leboncoin.core.model.Album
import fr.leboncoin.core.network.NetworkDataSource
import fr.leboncoin.core.network.dto.AlbumDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class AlbumRepositoryImpl(
    private val networkDataSource: NetworkDataSource,
    private val dispatcher: CoroutineDispatcher,
) : AlbumRepository {

    override suspend fun getAlbums(): List<Album> = withContext(dispatcher) {
        networkDataSource.getAlbums().map { it.toAlbum() }
    }
}

private fun AlbumDto.toAlbum() = Album(
    id = id,
    albumId = albumId,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
)
