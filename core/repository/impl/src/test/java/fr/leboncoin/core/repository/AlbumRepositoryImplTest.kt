package fr.leboncoin.core.repository

import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.network.NetworkDataSource
import fr.leboncoin.core.network.dto.AlbumDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AlbumRepositoryImplTest {

    private val networkDataSource = mockk<NetworkDataSource>()

    private fun newSut(testScheduler: kotlinx.coroutines.test.TestCoroutineScheduler) =
        AlbumRepositoryImpl(networkDataSource, StandardTestDispatcher(testScheduler))

    @Test
    fun `maps network dtos to domain albums`() = runTest {
        // Given
        coEvery { networkDataSource.getAlbums() } returns listOf(
            AlbumDto(id = 7, albumId = 3, title = "title", url = "url", thumbnailUrl = "thumb"),
        )

        // When
        val albums = newSut(testScheduler).getAlbums()

        // Then
        assertEquals(listOf(Album(id = 7, albumId = 3, title = "title", url = "url", thumbnailUrl = "thumb")), albums)
    }

    @Test
    fun `propagates network failures`() = runTest {
        // Given
        coEvery { networkDataSource.getAlbums() } throws DataUnavailableException()

        // When / Then
        val sut = newSut(testScheduler)
        var failure: Throwable? = null
        try {
            sut.getAlbums()
        } catch (e: DataUnavailableException) {
            failure = e
        }
        assertEquals(DataUnavailableException::class.java, failure?.javaClass)
    }
}
