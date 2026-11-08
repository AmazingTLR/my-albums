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

class GetAlbumPhotosUseCaseTest {

    private val albumRepository = mockk<AlbumRepository>()
    private val sut = GetAlbumPhotosUseCase(albumRepository)

    @Test
    fun `emits Success with the photos of the requested album`() = runTest {
        // Given
        val photos = listOf(photo(id = 1), photo(id = 2))
        every { albumRepository.observeAlbumPhotos(albumId = 3) } returns flowOf(photos)

        // When / Then
        sut(albumId = 3).test {
            assertEquals(GetAlbumPhotosUseCase.Result.Success(photos), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `emits Empty when the album has no photo`() = runTest {
        // Given
        every { albumRepository.observeAlbumPhotos(albumId = 3) } returns flowOf(emptyList())

        // When / Then
        sut(albumId = 3).test {
            assertEquals(GetAlbumPhotosUseCase.Result.Empty, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `emits a new result when a photo is favorited`() = runTest {
        // Given
        val before = listOf(photo(id = 1))
        val after = listOf(photo(id = 1, isFavorite = true))
        every { albumRepository.observeAlbumPhotos(albumId = 3) } returns flowOf(before, after)

        // When / Then
        sut(albumId = 3).test {
            assertEquals(GetAlbumPhotosUseCase.Result.Success(before), awaitItem())
            assertEquals(GetAlbumPhotosUseCase.Result.Success(after), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `propagates repository failures`() = runTest {
        // Given
        every { albumRepository.observeAlbumPhotos(albumId = 3) } returns flow { throw DataUnavailableException() }

        // When / Then
        sut(albumId = 3).test {
            assertTrue(awaitError() is DataUnavailableException)
        }
    }

    private fun photo(id: Int, isFavorite: Boolean = false) =
        Album(id = id, albumId = 3, title = "title $id", url = "url", thumbnailUrl = "thumb", isFavorite = isFavorite)
}
