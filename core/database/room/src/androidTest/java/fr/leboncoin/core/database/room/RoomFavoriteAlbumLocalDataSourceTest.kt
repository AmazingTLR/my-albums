package fr.leboncoin.core.database.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import fr.leboncoin.core.model.Album
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RoomFavoriteAlbumLocalDataSourceTest {

    private lateinit var database: AlbumDatabase
    private lateinit var albums: RoomAlbumLocalDataSource
    private lateinit var sut: RoomFavoriteAlbumLocalDataSource

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AlbumDatabase::class.java).build()
        albums = RoomAlbumLocalDataSource(database)
        sut = RoomFavoriteAlbumLocalDataSource(database.favoriteAlbumDao())
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun toggleAddsThenRemovesTheFavorite() = runTest {
        // Given / When
        sut.toggle(1)

        // Then
        assertEquals(setOf(1), sut.observeFavoriteIds().first())

        // When
        sut.toggle(1)

        // Then
        assertEquals(emptySet<Int>(), sut.observeFavoriteIds().first())
    }

    @Test
    fun observesTheStoredFavoriteAlbumsFlaggedAsFavorite() = runTest {
        // Given
        albums.replaceAll(listOf(album(1), album(2), album(3)))
        sut.toggle(3)
        sut.toggle(1)

        // When
        val favorites = sut.observeFavoriteAlbums().first()

        // Then
        assertEquals(listOf(album(1).copy(isFavorite = true), album(3).copy(isFavorite = true)), favorites)
    }

    @Test
    fun favoritesSurviveTheAlbumsBeingReplaced() = runTest {
        // Given
        albums.replaceAll(listOf(album(1)))
        sut.toggle(1)

        // When
        albums.replaceAll(listOf(album(1), album(2)))

        // Then
        assertEquals(listOf(album(1).copy(isFavorite = true)), sut.observeFavoriteAlbums().first())
    }

    @Test
    fun ignoresFavoritesWithoutAStoredAlbum() = runTest {
        // Given
        sut.toggle(42)

        // When / Then
        assertEquals(emptyList<Album>(), sut.observeFavoriteAlbums().first())
    }

    private fun album(id: Int) =
        Album(id = id, albumId = 1, title = "title $id", url = "url$id", thumbnailUrl = "thumb$id")
}
