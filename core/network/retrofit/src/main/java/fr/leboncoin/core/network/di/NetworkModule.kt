package fr.leboncoin.core.network.di

import retrofit2.converter.kotlinx.serialization.asConverterFactory
import fr.leboncoin.core.network.NetworkDataSource
import fr.leboncoin.core.network.retrofit.AlbumApiService
import fr.leboncoin.core.network.retrofit.RetrofitNetworkDataSource
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

private const val BASE_URL = "https://static.leboncoin.fr/"

/**
 * @param enableHttpLogging when true, request and response bodies are logged. Pass `BuildConfig.DEBUG`
 * from the app so that release builds never log network traffic.
 */
fun createNetworkModule(enableHttpLogging: Boolean) = module {
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .apply {
                if (enableHttpLogging) {
                    addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                }
            }
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<AlbumApiService> { get<Retrofit>().create<AlbumApiService>() }

    single<NetworkDataSource> { RetrofitNetworkDataSource(albumApiService = get<AlbumApiService>()) }
}
