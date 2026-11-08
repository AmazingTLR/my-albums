package fr.leboncoin.core.repository

import fr.leboncoin.core.datastore.UserPreferencesDataSource
import fr.leboncoin.core.model.DarkThemeConfig
import fr.leboncoin.core.model.UserData
import kotlinx.coroutines.flow.Flow

class UserDataRepositoryImpl(
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : UserDataRepository {

    override val userData: Flow<UserData> get() = userPreferencesDataSource.userData

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) =
        userPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
}
