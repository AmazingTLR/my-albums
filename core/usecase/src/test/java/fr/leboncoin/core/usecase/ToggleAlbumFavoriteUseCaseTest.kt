package fr.leboncoin.core.usecase

import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.repository.AlbumRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.fail
import org.junit.Test

class ToggleAlbumFavoriteUseCaseTest {

    private val albumRepository = mockk<AlbumRepository>(relaxUnitFun = true)
    private val sut = ToggleAlbumFavoriteUseCase(albumRepository)

    @Test
    fun `toggles the favorite of the requested album`() = runTest {
        // Given / When
        sut(albumId = 5)

        // Then
        coVerify(exactly = 1) { albumRepository.toggleAlbumFavorite(5) }
    }

    @Test
    fun `propagates repository failures`() = runTest {
        // Given
        coEvery { albumRepository.toggleAlbumFavorite(5) } throws DataUnavailableException()

        // When / Then
        try {
            sut(albumId = 5)
            fail("Expected a DataUnavailableException")
        } catch (_: DataUnavailableException) {
        }
    }
}
