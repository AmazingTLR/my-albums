package fr.leboncoin.feature.albums

import app.cash.turbine.test
import fr.leboncoin.core.analytics.AnalyticsEvent
import fr.leboncoin.core.analytics.AnalyticsLogger
import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.testing.CoroutinesRule
import fr.leboncoin.core.usecase.GetAlbumsUseCase
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

    private val getAlbumsUseCase = mockk<GetAlbumsUseCase>()
    private val analyticsLogger = mockk<AnalyticsLogger>(relaxUnitFun = true)

    private fun newSut() = AlbumsViewModel(getAlbumsUseCase, analyticsLogger)

    @Test
    fun `emits Success with the albums once loaded`() = runTest {
        // Given
        val albums = listOf(album(1), album(2))
        coEvery { getAlbumsUseCase() } returns GetAlbumsUseCase.Result.Success(albums)

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(AlbumsUiState.Success(albums), expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits an empty Success when there is no album`() = runTest {
        // Given
        coEvery { getAlbumsUseCase() } returns GetAlbumsUseCase.Result.Empty

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
        coEvery { getAlbumsUseCase() } throws DataUnavailableException()

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
        coEvery { getAlbumsUseCase() } returns GetAlbumsUseCase.Result.Empty
        val sut = newSut()

        // When
        sut.onAlbumSelected(album(42))

        // Then
        verify { analyticsLogger.logEvent(match<AnalyticsEvent> { it.extras.single().value == "42" }) }
    }

    private fun album(id: Int) = Album(id = id, albumId = 1, title = "title $id", url = "url", thumbnailUrl = "thumb")
}
