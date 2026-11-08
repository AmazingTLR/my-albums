package fr.leboncoin.core.repository

import fr.leboncoin.core.datastore.UserPreferencesDataSource
import fr.leboncoin.core.model.DarkThemeConfig
import fr.leboncoin.core.model.UserData
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class UserDataRepositoryImplTest {

    private val dataSource = mockk<UserPreferencesDataSource>(relaxUnitFun = true)
    private val sut = UserDataRepositoryImpl(dataSource)

    @Test
    fun `exposes the data source user data`() = runTest {
        // Given
        val userData = UserData(darkThemeConfig = DarkThemeConfig.DARK)
        every { dataSource.userData } returns flowOf(userData)

        // When
        val result = sut.userData.first()

        // Then
        assertEquals(userData, result)
    }

    @Test
    fun `delegates setDarkThemeConfig to the data source`() = runTest {
        // Given / When
        sut.setDarkThemeConfig(DarkThemeConfig.LIGHT)

        // Then
        coVerify(exactly = 1) { dataSource.setDarkThemeConfig(DarkThemeConfig.LIGHT) }
    }
}
