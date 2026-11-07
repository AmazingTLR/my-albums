package fr.leboncoin.core.usecase

import fr.leboncoin.core.model.AlbumGroup
import fr.leboncoin.core.repository.AlbumRepository

class GetAlbumGroupsUseCase(
    private val albumRepository: AlbumRepository,
) {
    sealed interface Result {
        data class Success(val groups: List<AlbumGroup>) : Result
        data object Empty : Result
    }

    /**
     * @throws fr.leboncoin.core.common.exception.AppException when the albums cannot be retrieved.
     */
    suspend operator fun invoke(): Result {
        val groups = albumRepository.getAlbumGroups(numberOfInitialCover = AlbumGroup.DEFAULT_NUMBER_OF_COVERS_PER_GROUP)
        return if (groups.isEmpty()) Result.Empty else Result.Success(groups)
    }
}
