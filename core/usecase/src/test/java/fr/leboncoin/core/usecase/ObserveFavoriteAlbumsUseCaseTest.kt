package fr.leboncoin.core.usecase

import app.cash.turbine.test
import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.repository.AlbumRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ObserveFavoriteAlbumsUseCaseTest {

    private val albumRepository = mockk<AlbumRepository>()
    private val sut = ObserveFavoriteAlbumsUseCase(albumRepository)

    @Test
    fun `emits Success with the favorite albums`() = runTest {
        // Given
        val albums = listOf(album(1), album(2))
        every { albumRepository.observeFavoriteAlbums() } returns flowOf(albums)

        // When / Then
        sut().test {
            assertEquals(ObserveFavoriteAlbumsUseCase.Result.Success(albums), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `emits Empty when there is no favorite`() = runTest {
        // Given
        every { albumRepository.observeFavoriteAlbums() } returns flowOf(emptyList())

        // When / Then
        sut().test {
            assertEquals(ObserveFavoriteAlbumsUseCase.Result.Empty, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `propagates repository failures`() = runTest {
        // Given
        every { albumRepository.observeFavoriteAlbums() } returns flow { throw DataUnavailableException() }

        // When / Then
        sut().test {
            assertTrue(awaitError() is DataUnavailableException)
        }
    }

    private fun album(id: Int) =
        Album(id = id, albumId = 1, title = "title $id", url = "url", thumbnailUrl = "thumb", isFavorite = true)
}
