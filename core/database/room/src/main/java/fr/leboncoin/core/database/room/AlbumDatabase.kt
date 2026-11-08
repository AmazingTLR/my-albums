package fr.leboncoin.core.database.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AlbumEntity::class, FavoriteAlbumEntity::class],
    version = 2,
    // 1 -> 2  adds the favorite_albums table
    autoMigrations = [AutoMigration(from = 1, to = 2)],
)
internal abstract class AlbumDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun favoriteAlbumDao(): FavoriteAlbumDao
}
