package fr.leboncoin.core.usecase

import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.model.AlbumGroup
import fr.leboncoin.core.repository.AlbumRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class GetAlbumGroupsUseCaseTest {

    private val albumRepository = mockk<AlbumRepository>()
    private val sut = GetAlbumGroupsUseCase(albumRepository)

    @Test
    fun `returns Success when the repository has groups`() = runTest {
        // Given
        val groups = listOf(group(albumId = 1), group(albumId = 2))
        coEvery { albumRepository.getAlbumGroups(numberOfInitialCover = 4) } returns groups

        // When
        val result = sut()

        // Then
        assertEquals(GetAlbumGroupsUseCase.Result.Success(groups), result)
    }

    @Test
    fun `returns Empty when the repository has no group`() = runTest {
        // Given
        coEvery { albumRepository.getAlbumGroups(numberOfInitialCover = 4) } returns emptyList()

        // When
        val result = sut()

        // Then
        assertEquals(GetAlbumGroupsUseCase.Result.Empty, result)
    }

    @Test
    fun `propagates repository failures`() = runTest {
        // Given
        coEvery { albumRepository.getAlbumGroups(numberOfInitialCover = 4) } throws DataUnavailableException()

        // When / Then
        try {
            sut()
            fail("Expected a DataUnavailableException")
        } catch (_: DataUnavailableException) {
        }
    }

    private fun group(albumId: Int) = AlbumGroup(albumId = albumId, photoCount = 50, coverUrls = listOf("thumb"))
}
