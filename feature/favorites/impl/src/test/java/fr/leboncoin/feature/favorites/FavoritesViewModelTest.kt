package fr.leboncoin.feature.favorites

import app.cash.turbine.test
import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.testing.CoroutinesRule
import fr.leboncoin.core.usecase.ObserveFavoriteAlbumsUseCase
import fr.leboncoin.core.usecase.ToggleAlbumFavoriteUseCase
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

class FavoritesViewModelTest {

    @get:Rule
    val coroutinesRule = CoroutinesRule()

    private val observeFavoriteAlbumsUseCase = mockk<ObserveFavoriteAlbumsUseCase>()
    private val toggleAlbumFavoriteUseCase = mockk<ToggleAlbumFavoriteUseCase>(relaxed = true)

    private fun newSut() = FavoritesViewModel(observeFavoriteAlbumsUseCase, toggleAlbumFavoriteUseCase)

    @Test
    fun `emits Success with the favorite albums`() = runTest {
        // Given
        val albums = listOf(album(1), album(2))
        every { observeFavoriteAlbumsUseCase() } returns flowOf(ObserveFavoriteAlbumsUseCase.Result.Success(albums))

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(FavoritesUiState.Success(albums), expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Empty when there is no favorite`() = runTest {
        // Given
        every { observeFavoriteAlbumsUseCase() } returns flowOf(ObserveFavoriteAlbumsUseCase.Result.Empty)

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(FavoritesUiState.Empty, expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits LoadFailed when observing the favorites fails`() = runTest {
        // Given
        every { observeFavoriteAlbumsUseCase() } returns flow { throw DataUnavailableException() }

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(FavoritesUiState.LoadFailed, expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggles the favorite of the clicked album`() = runTest {
        // Given
        every { observeFavoriteAlbumsUseCase() } returns flowOf(ObserveFavoriteAlbumsUseCase.Result.Empty)
        val sut = newSut()

        // When
        sut.onFavoriteClick(albumId = 4)

        // Then
        coVerify(exactly = 1) { toggleAlbumFavoriteUseCase(4) }
    }

    @Test
    fun `emits favoriteFailed when the favorite cannot be saved`() = runTest {
        // Given
        every { observeFavoriteAlbumsUseCase() } returns flowOf(ObserveFavoriteAlbumsUseCase.Result.Empty)
        coEvery { toggleAlbumFavoriteUseCase(4) } throws DataUnavailableException()
        val sut = newSut()

        sut.favoriteFailed.test {
            // When
            sut.onFavoriteClick(albumId = 4)

            // Then
            assertEquals(Unit, awaitItem())
        }
    }

    private fun album(id: Int) =
        Album(id = id, albumId = 1, title = "title $id", url = "url", thumbnailUrl = "thumb", isFavorite = true)
}
