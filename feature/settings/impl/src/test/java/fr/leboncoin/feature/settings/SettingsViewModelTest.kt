package fr.leboncoin.feature.settings

import app.cash.turbine.test
import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.model.DarkThemeConfig
import fr.leboncoin.core.model.UserData
import fr.leboncoin.core.testing.CoroutinesRule
import fr.leboncoin.core.usecase.ObserveUserDataUseCase
import fr.leboncoin.core.usecase.SetDarkThemeConfigUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val coroutinesRule = CoroutinesRule()

    private val observeUserDataUseCase = mockk<ObserveUserDataUseCase>()
    private val setDarkThemeConfigUseCase = mockk<SetDarkThemeConfigUseCase>(relaxed = true)

    private fun newSut() = SettingsViewModel(observeUserDataUseCase, setDarkThemeConfigUseCase)

    @Test
    fun `emits Success with the stored dark theme config`() = runTest {
        // Given
        every { observeUserDataUseCase() } returns flowOf(UserData(DarkThemeConfig.DARK))

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(SettingsUiState.Success(DarkThemeConfig.DARK), expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits LoadFailed when observing the settings fails`() = runTest {
        // Given
        every { observeUserDataUseCase() } returns flow { throw DataUnavailableException() }

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(SettingsUiState.LoadFailed, expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `stores the selected dark theme config`() = runTest {
        // Given
        every { observeUserDataUseCase() } returns flowOf(UserData(DarkThemeConfig.FOLLOW_SYSTEM))
        val sut = newSut()

        // When
        sut.updateDarkThemeConfig(DarkThemeConfig.LIGHT)

        // Then
        coVerify(exactly = 1) { setDarkThemeConfigUseCase(DarkThemeConfig.LIGHT) }
    }

    @Test
    fun `emits saveFailed when the dark theme config cannot be stored`() = runTest {
        // Given
        every { observeUserDataUseCase() } returns flowOf(UserData(DarkThemeConfig.FOLLOW_SYSTEM))
        coEvery { setDarkThemeConfigUseCase(DarkThemeConfig.DARK) } throws DataUnavailableException()
        val sut = newSut()

        sut.saveFailed.test {
            // When
            sut.updateDarkThemeConfig(DarkThemeConfig.DARK)

            // Then
            assertEquals(Unit, awaitItem())
        }
    }
}
