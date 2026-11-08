package fr.leboncoin.core.usecase

import fr.leboncoin.core.model.UserData
import fr.leboncoin.core.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow

class ObserveUserDataUseCase(
    private val userDataRepository: UserDataRepository,
) {
    operator fun invoke(): Flow<UserData> = userDataRepository.userData
}
