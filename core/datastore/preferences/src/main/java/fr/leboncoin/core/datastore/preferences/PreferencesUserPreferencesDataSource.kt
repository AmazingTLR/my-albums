package fr.leboncoin.core.datastore.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import fr.leboncoin.core.datastore.UserPreferencesDataSource
import fr.leboncoin.core.model.DarkThemeConfig
import fr.leboncoin.core.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal val DARK_THEME_CONFIG_KEY = stringPreferencesKey("dark_theme_config")

class PreferencesUserPreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) : UserPreferencesDataSource {

    override val userData: Flow<UserData> = dataStore.data
        .safeDataStoreFlow()
        .map { preferences -> UserData(darkThemeConfig = preferences[DARK_THEME_CONFIG_KEY].toDarkThemeConfig()) }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        safeDataStoreCall {
            dataStore.edit { preferences -> preferences[DARK_THEME_CONFIG_KEY] = darkThemeConfig.name }
        }
    }
}

// Follow system by default
private fun String?.toDarkThemeConfig(): DarkThemeConfig =
    DarkThemeConfig.entries.firstOrNull { it.name == this } ?: DarkThemeConfig.FOLLOW_SYSTEM
