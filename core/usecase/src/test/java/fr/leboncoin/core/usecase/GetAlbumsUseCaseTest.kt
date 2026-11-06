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

class GetAlbumsUseCaseTest {

    private val albumRepository = mockk<AlbumRepository>()
    private val sut = GetAlbumsUseCase(albumRepository)

    @Test
    fun `returns Success when the repository has albums`() = runTest {
        // Given
        val albums = listOf(album(id = 1), album(id = 2))
        coEvery { albumRepository.getAlbums() } returns albums

        // When
        val result = sut()

        // Then
        assertEquals(GetAlbumsUseCase.Result.Success(albums), result)
    }

    @Test
    fun `returns Empty when the repository has no album`() = runTest {
        // Given
        coEvery { albumRepository.getAlbums() } returns emptyList()

        // When
        val result = sut()

        // Then
        assertEquals(GetAlbumsUseCase.Result.Empty, result)
    }

    @Test
    fun `propagates repository failures`() = runTest {
        // Given
        coEvery { albumRepository.getAlbums() } throws DataUnavailableException()

        // When / Then
        try {
            sut()
            fail("Expected a DataUnavailableException")
        } catch (_: DataUnavailableException) {
        }
    }

    private fun album(id: Int) = Album(id = id, albumId = 1, title = "title $id", url = "url", thumbnailUrl = "thumb")
}
