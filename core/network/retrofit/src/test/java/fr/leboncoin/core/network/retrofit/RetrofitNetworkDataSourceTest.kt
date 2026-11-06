package fr.leboncoin.core.network.retrofit

import fr.leboncoin.core.common.exception.DataCorruptedException
import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.network.dto.AlbumDto
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

class RetrofitNetworkDataSourceTest {

    private lateinit var server: MockWebServer
    private lateinit var sut: RetrofitNetworkDataSource

    @Before
    fun setUp() {
        server = MockWebServer().apply { start() }
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
        sut = RetrofitNetworkDataSource(retrofit.create<AlbumApiService>())
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `parses the albums payload and ignores unknown keys`() = runTest {
        // Given
        server.enqueue(
            MockResponse().setBody(
                """[{"id":1,"albumId":2,"title":"t","url":"u","thumbnailUrl":"tu","extra":"ignored"}]""",
            ),
        )

        // When
        val albums = sut.getAlbums()

        // Then
        assertEquals(listOf(AlbumDto(id = 1, albumId = 2, title = "t", url = "u", thumbnailUrl = "tu")), albums)
        assertEquals("/img/shared/technical-test.json", server.takeRequest().path)
    }

    @Test
    fun `maps http errors to DataUnavailableException with the status code`() = runTest {
        // Given
        server.enqueue(MockResponse().setResponseCode(500))

        // When / Then
        try {
            sut.getAlbums()
            fail("Expected a DataUnavailableException")
        } catch (e: DataUnavailableException) {
            assertEquals("500", e.errorCode)
        }
    }

    @Test
    fun `maps malformed payloads to DataCorruptedException`() = runTest {
        // Given
        server.enqueue(MockResponse().setBody("""{"not":"a list"}"""))

        // When / Then
        try {
            sut.getAlbums()
            fail("Expected a DataCorruptedException")
        } catch (_: DataCorruptedException) {
        }
    }
}
