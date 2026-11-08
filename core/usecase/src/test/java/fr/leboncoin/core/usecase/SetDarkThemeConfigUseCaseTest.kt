package fr.leboncoin.core.usecase

import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.model.DarkThemeConfig
import fr.leboncoin.core.repository.UserDataRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.fail
import org.junit.Test

class SetDarkThemeConfigUseCaseTest {

    private val userDataRepository = mockk<UserDataRepository>(relaxUnitFun = true)
    private val sut = SetDarkThemeConfigUseCase(userDataRepository)

    @Test
    fun `stores the requested dark theme config`() = runTest {
        // Given / When
        sut(DarkThemeConfig.DARK)

        // Then
        coVerify(exactly = 1) { userDataRepository.setDarkThemeConfig(DarkThemeConfig.DARK) }
    }

    @Test
    fun `propagates repository failures`() = runTest {
        // Given
        coEvery { userDataRepository.setDarkThemeConfig(DarkThemeConfig.DARK) } throws DataUnavailableException()

        // When / Then
        try {
            sut(DarkThemeConfig.DARK)
            fail("Expected a DataUnavailableException")
        } catch (_: DataUnavailableException) {
        }
    }
}
