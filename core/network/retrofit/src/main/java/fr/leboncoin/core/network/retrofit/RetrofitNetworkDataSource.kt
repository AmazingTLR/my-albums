package fr.leboncoin.core.network.retrofit

import fr.leboncoin.core.network.NetworkDataSource
import fr.leboncoin.core.network.dto.AlbumDto
import fr.leboncoin.core.network.util.safeApiCall

internal class RetrofitNetworkDataSource(
    private val albumApiService: AlbumApiService,
) : NetworkDataSource {

    override suspend fun getAlbums(): List<AlbumDto> = safeApiCall { albumApiService.getAlbums() }
}
