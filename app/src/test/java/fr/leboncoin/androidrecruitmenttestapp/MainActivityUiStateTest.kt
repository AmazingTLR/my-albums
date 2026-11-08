package fr.leboncoin.androidrecruitmenttestapp

import fr.leboncoin.core.model.DarkThemeConfig
import fr.leboncoin.core.model.UserData
import org.junit.Assert.assertEquals
import org.junit.Test

class MainActivityUiStateTest {

    @Test
    fun `resolves the dark theme from the config and the system state`() {
        val cases = listOf(
            Triple(DarkThemeConfig.FOLLOW_SYSTEM, false, false),
            Triple(DarkThemeConfig.FOLLOW_SYSTEM, true, true),
            Triple(DarkThemeConfig.LIGHT, false, false),
            Triple(DarkThemeConfig.LIGHT, true, false),
            Triple(DarkThemeConfig.DARK, false, true),
            Triple(DarkThemeConfig.DARK, true, true),
        )

        cases.forEach { (config, systemDark, expected) ->
            // Given
            val sut = MainActivityUiState.Success(UserData(darkThemeConfig = config))

            // When
            val result = sut.shouldUseDarkTheme(isSystemDarkTheme = systemDark)

            // Then
            assertEquals("$config, system dark = $systemDark", expected, result)
        }
    }

    @Test
    fun `follows the system while loading`() {
        // Given / When / Then
        assertEquals(false, MainActivityUiState.Loading.shouldUseDarkTheme(isSystemDarkTheme = false))
        assertEquals(true, MainActivityUiState.Loading.shouldUseDarkTheme(isSystemDarkTheme = true))
    }
}
