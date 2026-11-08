package fr.leboncoin.core.repository

import fr.leboncoin.core.model.DarkThemeConfig
import fr.leboncoin.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>

    /**
     * @throws fr.leboncoin.core.common.exception.AppException when the preference cannot be stored.
     */
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)
}
