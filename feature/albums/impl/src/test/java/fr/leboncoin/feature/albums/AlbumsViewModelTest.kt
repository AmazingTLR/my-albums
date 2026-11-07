package fr.leboncoin.feature.albums

import app.cash.turbine.test
import fr.leboncoin.core.analytics.AnalyticsEvent
import fr.leboncoin.core.analytics.AnalyticsLogger
import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.model.AlbumGroup
import fr.leboncoin.core.testing.CoroutinesRule
import fr.leboncoin.core.usecase.GetAlbumGroupsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AlbumsViewModelTest {

    @get:Rule
    val coroutinesRule = CoroutinesRule()

    private val getAlbumGroupsUseCase = mockk<GetAlbumGroupsUseCase>()
    private val analyticsLogger = mockk<AnalyticsLogger>(relaxUnitFun = true)

    private fun newSut() = AlbumsViewModel(getAlbumGroupsUseCase, analyticsLogger)

    @Test
    fun `emits Success with the album groups once loaded`() = runTest {
        // Given
        val groups = listOf(group(1), group(2))
        coEvery { getAlbumGroupsUseCase() } returns GetAlbumGroupsUseCase.Result.Success(groups)

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(AlbumsUiState.Success(groups), expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits an empty Success when there is no album`() = runTest {
        // Given
        coEvery { getAlbumGroupsUseCase() } returns GetAlbumGroupsUseCase.Result.Empty

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(AlbumsUiState.Success(emptyList()), expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits LoadFailed when loading fails`() = runTest {
        // Given
        coEvery { getAlbumGroupsUseCase() } throws DataUnavailableException()

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(AlbumsUiState.LoadFailed, expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `logs an analytics event when an album is selected`() {
        // Given
        coEvery { getAlbumGroupsUseCase() } returns GetAlbumGroupsUseCase.Result.Empty
        val sut = newSut()

        // When
        sut.onAlbumSelected(42)

        // Then
        verify { analyticsLogger.logEvent(match<AnalyticsEvent> { it.extras.single().value == "42" }) }
    }

    private fun group(albumId: Int) = AlbumGroup(albumId = albumId, photoCount = 50, coverUrls = listOf("thumb"))
}
