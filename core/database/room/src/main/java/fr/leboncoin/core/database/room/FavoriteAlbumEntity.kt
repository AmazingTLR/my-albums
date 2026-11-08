package fr.leboncoin.core.database.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_albums")
internal data class FavoriteAlbumEntity(@PrimaryKey val id: Int)
