package fr.leboncoin.core.datastore.preferences

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import fr.leboncoin.core.model.DarkThemeConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class PreferencesUserPreferencesDataSourceTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
    private val dataStore by lazy {
        PreferenceDataStoreFactory.create(scope = scope) { File(temporaryFolder.root, "test.preferences_pb") }
    }
    private val sut by lazy { PreferencesUserPreferencesDataSource(dataStore) }

    @After
    fun tearDown() = scope.cancel()

    @Test
    fun `follows the system on a fresh install`() = runTest {
        // Given / When
        val userData = sut.userData.first()

        // Then
        assertEquals(DarkThemeConfig.FOLLOW_SYSTEM, userData.darkThemeConfig)
    }

    @Test
    fun `restores the stored dark theme config`() = runTest {
        DarkThemeConfig.entries.forEach { config ->
            // Given / When
            sut.setDarkThemeConfig(config)

            // Then
            assertEquals(config, sut.userData.first().darkThemeConfig)
        }
    }

    @Test
    fun `falls back to following the system on an unknown stored value`() = runTest {
        // Given
        dataStore.edit { it[DARK_THEME_CONFIG_KEY] = "SEPIA" }

        // When
        val userData = sut.userData.first()

        // Then
        assertEquals(DarkThemeConfig.FOLLOW_SYSTEM, userData.darkThemeConfig)
    }
}
