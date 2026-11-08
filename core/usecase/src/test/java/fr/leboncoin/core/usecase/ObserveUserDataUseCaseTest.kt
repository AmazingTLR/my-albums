package fr.leboncoin.core.usecase

import fr.leboncoin.core.model.DarkThemeConfig
import fr.leboncoin.core.model.UserData
import fr.leboncoin.core.repository.UserDataRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ObserveUserDataUseCaseTest {

    private val userDataRepository = mockk<UserDataRepository>()
    private val sut = ObserveUserDataUseCase(userDataRepository)

    @Test
    fun `emits the repository user data`() = runTest {
        // Given
        val userData = UserData(darkThemeConfig = DarkThemeConfig.LIGHT)
        every { userDataRepository.userData } returns flowOf(userData)

        // When
        val result = sut().first()

        // Then
        assertEquals(userData, result)
    }
}
