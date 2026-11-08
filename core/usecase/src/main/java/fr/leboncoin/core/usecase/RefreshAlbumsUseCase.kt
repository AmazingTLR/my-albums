package fr.leboncoin.core.usecase

import fr.leboncoin.core.common.exception.AppException
import fr.leboncoin.core.repository.AlbumRepository
import kotlin.coroutines.cancellation.CancellationException

class RefreshAlbumsUseCase(
    private val albumRepository: AlbumRepository,
) {
    sealed interface Result {
        data object Success : Result
        data object Failure : Result
    }

    suspend operator fun invoke(): Result =
        try {
            albumRepository.refreshAlbums()
            Result.Success
        } catch (e: CancellationException) {
            throw e
        } catch (_: AppException) {
            Result.Failure
        }
}
