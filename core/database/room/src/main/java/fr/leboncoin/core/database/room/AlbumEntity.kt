package fr.leboncoin.core.database.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import fr.leboncoin.core.model.Album

// albumId is indexed so grouping and per-album queries never scan the whole data
@Entity(tableName = "albums", indices = [Index(value = ["albumId"])])
internal data class AlbumEntity(
    @PrimaryKey val id: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String,
)

internal fun Album.toEntity() = AlbumEntity(
    id = id,
    albumId = albumId,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
)

internal fun AlbumEntity.toAlbum() = Album(
    id = id,
    albumId = albumId,
    title = title,
    url = url,
    thumbnailUrl = thumbnailUrl,
)
