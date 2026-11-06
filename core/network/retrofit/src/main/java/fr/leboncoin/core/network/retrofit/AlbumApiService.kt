package fr.leboncoin.core.network.retrofit

import fr.leboncoin.core.network.dto.AlbumDto
import retrofit2.http.GET

internal interface AlbumApiService {

    @GET("img/shared/technical-test.json")
    suspend fun getAlbums(): List<AlbumDto>
}
