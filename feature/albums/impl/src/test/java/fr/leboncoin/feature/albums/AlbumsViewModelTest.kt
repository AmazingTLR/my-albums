package fr.leboncoin.feature.albums

import app.cash.turbine.test
import fr.leboncoin.core.analytics.AnalyticsEvent
import fr.leboncoin.core.analytics.AnalyticsLogger
import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.model.AlbumGroup
import fr.leboncoin.core.testing.CoroutinesRule
import fr.leboncoin.core.usecase.GetAlbumGroupsUseCase
import fr.leboncoin.core.usecase.RefreshAlbumsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
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
    private val refreshAlbumsUseCase = mockk<RefreshAlbumsUseCase>()
    private val analyticsLogger = mockk<AnalyticsLogger>(relaxUnitFun = true)

    private fun newSut() = AlbumsViewModel(getAlbumGroupsUseCase, refreshAlbumsUseCase, analyticsLogger)

    @Test
    fun `shows the stored groups then refreshes them in the background`() = runTest {
        // Given
        val stored = listOf(group(1))
        val refreshed = listOf(group(1), group(2))
        coEvery { getAlbumGroupsUseCase() } returnsMany listOf(
            GetAlbumGroupsUseCase.Result.Success(stored),
            GetAlbumGroupsUseCase.Result.Success(refreshed),
        )
        coEvery { refreshAlbumsUseCase() } returns RefreshAlbumsUseCase.Result.Success

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            // Intermediate states are conflated by the StateFlow: only the settled one is observable.
            assertEquals(AlbumsUiState.Success(refreshed), expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { refreshAlbumsUseCase() }
    }

    @Test
    fun `keeps the stored groups when the background refresh fails`() = runTest {
        // Given
        val stored = listOf(group(1))
        coEvery { getAlbumGroupsUseCase() } returns GetAlbumGroupsUseCase.Result.Success(stored)
        coEvery { refreshAlbumsUseCase() } returns RefreshAlbumsUseCase.Result.Failure

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(AlbumsUiState.Success(stored), expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `downloads before showing anything when nothing is stored`() = runTest {
        // Given
        val downloaded = listOf(group(1), group(2))
        coEvery { getAlbumGroupsUseCase() } returnsMany listOf(
            GetAlbumGroupsUseCase.Result.Empty,
            GetAlbumGroupsUseCase.Result.Success(downloaded),
        )
        coEvery { refreshAlbumsUseCase() } returns RefreshAlbumsUseCase.Result.Success

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(AlbumsUiState.Success(downloaded), expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { refreshAlbumsUseCase() }
    }

    @Test
    fun `emits LoadFailed when nothing is stored and the download fails`() = runTest {
        // Given
        coEvery { getAlbumGroupsUseCase() } returns GetAlbumGroupsUseCase.Result.Empty
        coEvery { refreshAlbumsUseCase() } returns RefreshAlbumsUseCase.Result.Failure

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(AlbumsUiState.LoadFailed, expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits LoadFailed when the storage cannot be read`() = runTest {
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
    fun `onRefresh downloads again and updates the groups`() = runTest {
        // Given
        val stored = listOf(group(1))
        val refreshed = listOf(group(1), group(2))
        coEvery { getAlbumGroupsUseCase() } returnsMany listOf(
            GetAlbumGroupsUseCase.Result.Success(stored),
            GetAlbumGroupsUseCase.Result.Success(stored),
            GetAlbumGroupsUseCase.Result.Success(refreshed),
        )
        coEvery { refreshAlbumsUseCase() } returns RefreshAlbumsUseCase.Result.Success
        val sut = newSut()

        sut.uiState.test {
            // When
            assertEquals(AlbumsUiState.Success(stored), expectMostRecentItem())
            sut.onRefresh()

            // Then
            assertEquals(AlbumsUiState.Success(refreshed), expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 2) { refreshAlbumsUseCase() }
    }

    @Test
    fun `logs an analytics event when an album is selected`() {
        // Given
        coEvery { getAlbumGroupsUseCase() } returns GetAlbumGroupsUseCase.Result.Empty
        coEvery { refreshAlbumsUseCase() } returns RefreshAlbumsUseCase.Result.Failure
        val sut = newSut()

        // When
        sut.onAlbumSelected(42)

        // Then
        verify { analyticsLogger.logEvent(match<AnalyticsEvent> { it.extras.single().value == "42" }) }
    }

    private fun group(albumId: Int) = AlbumGroup(albumId = albumId, photoCount = 50, coverUrls = listOf("thumb"))
}
