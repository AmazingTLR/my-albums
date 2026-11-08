package fr.leboncoin.core.usecase

import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.repository.AlbumRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class RefreshAlbumsUseCaseTest {

    private val albumRepository = mockk<AlbumRepository>()
    private val sut = RefreshAlbumsUseCase(albumRepository)

    @Test
    fun `returns Success when the repository refreshes`() = runTest {
        // Given
        coEvery { albumRepository.refreshAlbums() } returns Unit

        // When
        val result = sut()

        // Then
        assertEquals(RefreshAlbumsUseCase.Result.Success, result)
        coVerify { albumRepository.refreshAlbums() }
    }

    @Test
    fun `returns Failure when the repository fails`() = runTest {
        // Given
        coEvery { albumRepository.refreshAlbums() } throws DataUnavailableException()

        // When
        val result = sut()

        // Then
        assertEquals(RefreshAlbumsUseCase.Result.Failure, result)
    }

    @Test
    fun `rethrows cancellation`() = runTest {
        // Given
        coEvery { albumRepository.refreshAlbums() } throws CancellationException()

        // When / Then
        try {
            sut()
            fail("Expected a CancellationException")
        } catch (_: CancellationException) {
        }
    }
}
