package fr.leboncoin.core.model

data class AlbumGroup(
    val albumId: Int,
    val photoCount: Int,
    val coverUrls: List<String>,
) {
    companion object {
        const val DEFAULT_NUMBER_OF_COVERS_PER_GROUP = 4
    }
}
