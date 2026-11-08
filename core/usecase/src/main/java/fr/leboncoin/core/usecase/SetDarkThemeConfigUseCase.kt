package fr.leboncoin.core.usecase

import fr.leboncoin.core.model.DarkThemeConfig
import fr.leboncoin.core.repository.UserDataRepository

class SetDarkThemeConfigUseCase(
    private val userDataRepository: UserDataRepository,
) {
    /**
     * @throws fr.leboncoin.core.common.exception.AppException when the preference cannot be stored.
     */
    suspend operator fun invoke(darkThemeConfig: DarkThemeConfig) =
        userDataRepository.setDarkThemeConfig(darkThemeConfig)
}
