package fr.leboncoin.core.repository

import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.database.AlbumLocalDataSource
import fr.leboncoin.core.database.FavoriteAlbumLocalDataSource
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.model.AlbumGroup
import fr.leboncoin.core.network.NetworkDataSource
import fr.leboncoin.core.network.dto.AlbumDto
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class AlbumRepositoryImplTest {

    private companion object {
        const val NUMBER_OF_COVERS = 3
    }

    private val networkDataSource = mockk<NetworkDataSource>()
    private val localDataSource = mockk<AlbumLocalDataSource>(relaxUnitFun = true)
    private val favoriteDataSource = mockk<FavoriteAlbumLocalDataSource>(relaxUnitFun = true)

    private fun TestScope.newSut() =
        AlbumRepositoryImpl(
            networkDataSource,
            localDataSource,
            favoriteDataSource,
            StandardTestDispatcher(testScheduler),
        )

    @Test
    fun `reads the groups from storage without calling the network`() = runTest {
        // Given
        coEvery { localDataSource.getAlbumGroups(NUMBER_OF_COVERS) } returns listOf(group(albumId = 3))

        // When
        val groups = newSut().getAlbumGroups(NUMBER_OF_COVERS)

        // Then
        assertEquals(listOf(group(albumId = 3)), groups)
        coVerify(exactly = 0) { networkDataSource.getAlbums() }
    }

    @Test
    fun `refresh stores the mapped network albums`() = runTest {
        // Given
        coEvery { networkDataSource.getAlbums() } returns listOf(dto(id = 7, albumId = 3))

        // When
        newSut().refreshAlbums()

        // Then
        coVerify { localDataSource.replaceAll(listOf(album(id = 7, albumId = 3))) }
    }

    @Test
    fun `refresh propagates the network failure and keeps the stored albums`() = runTest {
        // Given
        val failure = DataUnavailableException()
        coEvery { networkDataSource.getAlbums() } throws failure

        // When / Then
        try {
            newSut().refreshAlbums()
            fail("Expected a DataUnavailableException")
        } catch (e: DataUnavailableException) {
            assertSame(failure, e)
        }
        coVerify(exactly = 0) { localDataSource.replaceAll(any()) }
    }

    @Test
    fun `observes the stored photos of an album without calling the network`() = runTest {
        // Given
        val photos = listOf(album(id = 1, albumId = 3), album(id = 2, albumId = 3))
        coEvery { localDataSource.getAlbumsById(albumId = 3) } returns photos
        every { localDataSource.observeAlbumsById(3) } returns flowOf(photos)
        every { favoriteDataSource.observeFavoriteIds() } returns flowOf(emptySet())

        // When / Then
        newSut().observeAlbumPhotos(albumId = 3).test {
            assertEquals(photos, awaitItem())
            awaitComplete()
        }
        coVerify(exactly = 0) { networkDataSource.getAlbums() }
    }

    @Test
    fun `flags the favorite photos and follows favorite changes`() = runTest {
        // Given
        val photos = listOf(album(id = 1, albumId = 3), album(id = 2, albumId = 3))
        val favoriteIds = MutableStateFlow(setOf(2))
        coEvery { localDataSource.getAlbumsById(albumId = 3) } returns photos
        every { localDataSource.observeAlbumsById(3) } returns flowOf(photos)
        every { favoriteDataSource.observeFavoriteIds() } returns favoriteIds

        // When / Then
        newSut().observeAlbumPhotos(albumId = 3).test {
            assertEquals(listOf(photos[0], photos[1].copy(isFavorite = true)), awaitItem())

            favoriteIds.value = setOf(1, 2)
            assertEquals(photos.map { it.copy(isFavorite = true) }, awaitItem())

            favoriteIds.value = emptySet()
            assertEquals(photos, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetches from the network when the album is not stored yet`() = runTest {
        // Given
        val photos = listOf(album(id = 1, albumId = 3))
        coEvery { localDataSource.getAlbumsById(albumId = 3) } returnsMany listOf(emptyList(), photos)
        coEvery { networkDataSource.getAlbums() } returns listOf(dto(id = 1, albumId = 3))
        every { localDataSource.observeAlbumsById(3) } returns flowOf(photos)
        every { favoriteDataSource.observeFavoriteIds() } returns flowOf(emptySet())

        // When / Then
        newSut().observeAlbumPhotos(albumId = 3).test {
            assertEquals(photos, awaitItem())
            awaitComplete()
        }
        coVerify { localDataSource.replaceAll(photos) }
    }

    @Test
    fun `fails when the album is not stored and the network fails`() = runTest {
        // Given
        coEvery { localDataSource.getAlbumsById(albumId = 3) } returns emptyList()
        coEvery { networkDataSource.getAlbums() } throws DataUnavailableException()

        // When / Then
        newSut().observeAlbumPhotos(albumId = 3).test {
            assertTrue(awaitError() is DataUnavailableException)
        }
    }

    @Test
    fun `observes the favorite albums from storage`() = runTest {
        // Given
        val favorites = listOf(album(id = 1, albumId = 3).copy(isFavorite = true))
        every { favoriteDataSource.observeFavoriteAlbums() } returns flowOf(favorites)

        // When / Then
        newSut().observeFavoriteAlbums().test {
            assertEquals(favorites, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `toggles the favorite in storage`() = runTest {
        // Given / When
        newSut().toggleAlbumFavorite(albumId = 4)

        // Then
        coVerify(exactly = 1) { favoriteDataSource.toggle(4) }
    }

    private fun dto(id: Int, albumId: Int) =
        AlbumDto(id = id, albumId = albumId, title = "title", url = "url", thumbnailUrl = "thumb")

    private fun album(id: Int, albumId: Int) =
        Album(id = id, albumId = albumId, title = "title", url = "url", thumbnailUrl = "thumb")

    private fun group(albumId: Int) = AlbumGroup(albumId = albumId, photoCount = 1, coverUrls = listOf("thumb"))
}
