package fr.leboncoin.core.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class FavoriteAlbumDao {

    // Returns -1 when the row already exists.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun tryAdd(favorite: FavoriteAlbumEntity): Long

    @Query("DELETE FROM favorite_albums WHERE id = :albumId")
    protected abstract suspend fun remove(albumId: Int)

    // Insert first: an ignored insert means it already was a favorite, so remove it. No read-then-write.
    @Transaction
    open suspend fun toggle(albumId: Int) {
        if (tryAdd(FavoriteAlbumEntity(albumId)) == -1L) remove(albumId)
    }

    @Query("SELECT id FROM favorite_albums")
    abstract fun observeFavoriteIds(): Flow<List<Int>>

    @Query(
        """
        SELECT albums.* FROM albums
        INNER JOIN favorite_albums ON albums.id = favorite_albums.id
        ORDER BY albums.id
        """,
    )
    abstract fun observeFavoriteAlbums(): Flow<List<AlbumEntity>>
}
