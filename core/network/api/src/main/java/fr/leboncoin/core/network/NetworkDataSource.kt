package fr.leboncoin.core.network

import fr.leboncoin.core.network.dto.AlbumDto

interface NetworkDataSource {
    suspend fun getAlbums(): List<AlbumDto>
}
