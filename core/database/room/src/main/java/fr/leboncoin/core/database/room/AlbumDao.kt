package fr.leboncoin.core.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

internal data class AlbumCountRow(val albumId: Int, val photoCount: Int)

internal data class AlbumCoverRow(val albumId: Int, val thumbnailUrl: String)

@Dao
internal abstract class AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertAll(albums: List<AlbumEntity>)

    @Query("DELETE FROM albums")
    protected abstract suspend fun deleteAll()

    @Transaction
    open suspend fun replaceAll(albums: List<AlbumEntity>) {
        deleteAll()
        insertAll(albums)
    }

    @Query("SELECT albumId, COUNT(*) AS photoCount FROM albums GROUP BY albumId ORDER BY albumId")
    abstract suspend fun getAlbumCounts(): List<AlbumCountRow>

    // get thumbnails of the first [numberOfInitialCover] photos of every album.
    @Query(
        """
        SELECT albumId, thumbnailUrl FROM albums
        WHERE (SELECT COUNT(*) FROM albums AS previous
               WHERE previous.albumId = albums.albumId AND previous.id < albums.id) < :numberOfInitialCover
        ORDER BY albumId, id
        """,
    )
    abstract suspend fun getAlbumCovers(numberOfInitialCover: Int): List<AlbumCoverRow>

    @Query("SELECT * FROM albums WHERE albumId = :albumId ORDER BY id")
    abstract suspend fun getAlbumsById(albumId: Int): List<AlbumEntity>
}
