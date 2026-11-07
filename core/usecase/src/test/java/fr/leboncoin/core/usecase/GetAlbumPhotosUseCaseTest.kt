package fr.leboncoin.core.usecase

import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.repository.AlbumRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class GetAlbumPhotosUseCaseTest {

    private val albumRepository = mockk<AlbumRepository>()
    private val sut = GetAlbumPhotosUseCase(albumRepository)

    @Test
    fun `returns Success with the photos of the requested album`() = runTest {
        // Given
        val photos = listOf(photo(id = 1), photo(id = 2))
        coEvery { albumRepository.getAlbumPhotos(albumId = 3) } returns photos

        // When
        val result = sut(albumId = 3)

        // Then
        assertEquals(GetAlbumPhotosUseCase.Result.Success(photos), result)
    }

    @Test
    fun `returns Empty when the album has no photo`() = runTest {
        // Given
        coEvery { albumRepository.getAlbumPhotos(albumId = 3) } returns emptyList()

        // When
        val result = sut(albumId = 3)

        // Then
        assertEquals(GetAlbumPhotosUseCase.Result.Empty, result)
    }

    @Test
    fun `propagates repository failures`() = runTest {
        // Given
        coEvery { albumRepository.getAlbumPhotos(albumId = 3) } throws DataUnavailableException()

        // When / Then
        try {
            sut(albumId = 3)
            fail("Expected a DataUnavailableException")
        } catch (_: DataUnavailableException) {
        }
    }

    private fun photo(id: Int) = Album(id = id, albumId = 3, title = "title $id", url = "url", thumbnailUrl = "thumb")
}
