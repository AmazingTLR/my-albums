package fr.leboncoin.core.repository

import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.database.AlbumLocalDataSource
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.model.AlbumGroup
import fr.leboncoin.core.network.NetworkDataSource
import fr.leboncoin.core.network.dto.AlbumDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.fail
import org.junit.Test

class AlbumRepositoryImplTest {

    private companion object {
        const val NUMBER_OF_COVERS = 3
    }

    private val networkDataSource = mockk<NetworkDataSource>()
    private val localDataSource = mockk<AlbumLocalDataSource>(relaxUnitFun = true)

    private fun TestScope.newSut() =
        AlbumRepositoryImpl(networkDataSource, localDataSource, StandardTestDispatcher(testScheduler))

    @Test
    fun `stores the mapped network albums then reads the groups from storage`() = runTest {
        // Given
        coEvery { networkDataSource.getAlbums() } returns listOf(dto(id = 7, albumId = 3))
        coEvery { localDataSource.getAlbumGroups(NUMBER_OF_COVERS) } returns listOf(group(albumId = 3))

        // When
        val groups = newSut().getAlbumGroups(NUMBER_OF_COVERS)

        // Then
        assertEquals(listOf(group(albumId = 3)), groups)
        coVerifyOrder {
            localDataSource.replaceAll(listOf(album(id = 7, albumId = 3)))
            localDataSource.getAlbumGroups(NUMBER_OF_COVERS)
        }
    }

    @Test
    fun `falls back to the stored groups when the network fails`() = runTest {
        // Given
        coEvery { networkDataSource.getAlbums() } throws DataUnavailableException()
        coEvery { localDataSource.getAlbumGroups(NUMBER_OF_COVERS) } returns listOf(group(albumId = 1))

        // When
        val groups = newSut().getAlbumGroups(NUMBER_OF_COVERS)

        // Then
        assertEquals(listOf(group(albumId = 1)), groups)
        coVerify(exactly = 0) { localDataSource.replaceAll(any()) }
    }

    @Test
    fun `propagates the network failure when nothing is stored`() = runTest {
        // Given
        val failure = DataUnavailableException()
        coEvery { networkDataSource.getAlbums() } throws failure
        coEvery { localDataSource.getAlbumGroups(NUMBER_OF_COVERS) } returns emptyList()

        // When / Then
        try {
            newSut().getAlbumGroups(NUMBER_OF_COVERS)
            fail("Expected a DataUnavailableException")
        } catch (e: DataUnavailableException) {
            assertSame(failure, e)
        }
    }

    @Test
    fun `returns the stored photos of an album without calling the network`() = runTest {
        // Given
        val photos = listOf(album(id = 1, albumId = 3), album(id = 2, albumId = 3))
        coEvery { localDataSource.getAlbumsById(albumId = 3) } returns photos

        // When
        val result = newSut().getAlbumPhotos(albumId = 3)

        // Then
        assertEquals(photos, result)
        coVerify(exactly = 0) { networkDataSource.getAlbums() }
    }

    @Test
    fun `fetches from the network when the album is not stored yet`() = runTest {
        // Given
        val photos = listOf(album(id = 1, albumId = 3))
        coEvery { localDataSource.getAlbumsById(albumId = 3) } returnsMany listOf(emptyList(), photos)
        coEvery { networkDataSource.getAlbums() } returns listOf(dto(id = 1, albumId = 3))

        // When
        val result = newSut().getAlbumPhotos(albumId = 3)

        // Then
        assertEquals(photos, result)
        coVerify { localDataSource.replaceAll(photos) }
    }

    @Test
    fun `propagates the network failure when the album is not stored and the network fails`() = runTest {
        // Given
        coEvery { localDataSource.getAlbumsById(albumId = 3) } returns emptyList()
        coEvery { networkDataSource.getAlbums() } throws DataUnavailableException()

        // When / Then
        try {
            newSut().getAlbumPhotos(albumId = 3)
            fail("Expected a DataUnavailableException")
        } catch (_: DataUnavailableException) {
        }
    }

    private fun dto(id: Int, albumId: Int) =
        AlbumDto(id = id, albumId = albumId, title = "title", url = "url", thumbnailUrl = "thumb")

    private fun album(id: Int, albumId: Int) =
        Album(id = id, albumId = albumId, title = "title", url = "url", thumbnailUrl = "thumb")

    private fun group(albumId: Int) = AlbumGroup(albumId = albumId, photoCount = 1, coverUrls = listOf("thumb"))
}
