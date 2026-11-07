package fr.leboncoin.core.database.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AlbumEntity::class], version = 1)
internal abstract class AlbumDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}
