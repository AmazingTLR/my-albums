package fr.leboncoin.core.database.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.model.AlbumGroup
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RoomAlbumLocalDataSourceTest {

    private lateinit var database: AlbumDatabase
    private lateinit var sut: RoomAlbumLocalDataSource

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AlbumDatabase::class.java).build()
        sut = RoomAlbumLocalDataSource(database)
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun groupsPhotosByAlbumIdWithCountAndFirstCovers() = runTest {
        // Given
        val albums = (1..6).map { album(id = it, albumId = 1) } + album(id = 7, albumId = 2)
        sut.replaceAll(albums)

        // When
        val groups = sut.getAlbumGroups(numberOfInitialCover = 4)

        // Then
        assertEquals(
            listOf(
                AlbumGroup(albumId = 1, photoCount = 6, coverUrls = (1..4).map { "thumb$it" }),
                AlbumGroup(albumId = 2, photoCount = 1, coverUrls = listOf("thumb7")),
            ),
            groups,
        )
    }

    @Test
    fun returnsOnlyThePhotosOfTheRequestedAlbum() = runTest {
        // Given
        sut.replaceAll(listOf(album(id = 1, albumId = 1), album(id = 2, albumId = 2), album(id = 3, albumId = 2)))

        // When
        val photos = sut.getAlbumsById(albumId = 2)

        // Then
        assertEquals(listOf(album(id = 2, albumId = 2), album(id = 3, albumId = 2)), photos)
    }

    @Test
    fun replaceAllDiscardsThePreviousContent() = runTest {
        // Given
        sut.replaceAll(listOf(album(id = 1, albumId = 1)))

        // When
        sut.replaceAll(listOf(album(id = 2, albumId = 5)))

        // Then
        assertEquals(listOf(5), sut.getAlbumGroups(numberOfInitialCover = 4).map { it.albumId })
    }

    private fun album(id: Int, albumId: Int) =
        Album(id = id, albumId = albumId, title = "title $id", url = "url$id", thumbnailUrl = "thumb$id")
}
