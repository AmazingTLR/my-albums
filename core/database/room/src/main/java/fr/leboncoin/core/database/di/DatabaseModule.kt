package fr.leboncoin.core.database.di

import androidx.room.Room
import fr.leboncoin.core.database.AlbumLocalDataSource
import fr.leboncoin.core.database.room.AlbumDatabase
import fr.leboncoin.core.database.room.RoomAlbumLocalDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val DATABASE_NAME = "albums.db"

val databaseModule = module {
    single<AlbumDatabase> {
        Room.databaseBuilder(androidContext(), AlbumDatabase::class.java, DATABASE_NAME).build()
    }
    single<AlbumLocalDataSource> { RoomAlbumLocalDataSource(database = get()) }
}
