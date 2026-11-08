package fr.leboncoin.feature.details

import app.cash.turbine.test
import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.testing.CoroutinesRule
import fr.leboncoin.core.usecase.GetAlbumPhotosUseCase
import fr.leboncoin.core.usecase.ToggleAlbumFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DetailsViewModelTest {

    @get:Rule
    val coroutinesRule = CoroutinesRule()

    private val getAlbumPhotosUseCase = mockk<GetAlbumPhotosUseCase>()
    private val toggleAlbumFavoriteUseCase = mockk<ToggleAlbumFavoriteUseCase>(relaxed = true)

    private fun newSut() = DetailsViewModel(
        albumId = ALBUM_ID,
        getAlbumPhotosUseCase = getAlbumPhotosUseCase,
        toggleAlbumFavoriteUseCase = toggleAlbumFavoriteUseCase,
    )

    @Test
    fun `emits Success with the album photos once loaded`() = runTest {
        // Given
        val photos = listOf(photo(1), photo(2))
        every { getAlbumPhotosUseCase(ALBUM_ID) } returns flowOf(GetAlbumPhotosUseCase.Result.Success(photos))

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(DetailsUiState.Success(photos), expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Success again with the updated photos when a favorite changes`() = runTest {
        // Given
        val results = MutableSharedFlow<GetAlbumPhotosUseCase.Result>()
        every { getAlbumPhotosUseCase(ALBUM_ID) } returns results
        val sut = newSut()

        sut.uiState.test {
            assertEquals(DetailsUiState.Loading, awaitItem())

            // When
            val before = listOf(photo(1))
            val after = listOf(photo(1).copy(isFavorite = true))
            results.emit(GetAlbumPhotosUseCase.Result.Success(before))
            results.emit(GetAlbumPhotosUseCase.Result.Success(after))

            // Then
            assertEquals(DetailsUiState.Success(before), awaitItem())
            assertEquals(DetailsUiState.Success(after), awaitItem())
        }
    }

    @Test
    fun `emits Empty when the album has no photo`() = runTest {
        // Given
        every { getAlbumPhotosUseCase(ALBUM_ID) } returns flowOf(GetAlbumPhotosUseCase.Result.Empty)

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(DetailsUiState.Empty, expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits LoadFailed when loading fails`() = runTest {
        // Given
        every { getAlbumPhotosUseCase(ALBUM_ID) } returns flow { throw DataUnavailableException() }

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(DetailsUiState.LoadFailed, expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggles the favorite of the clicked photo`() = runTest {
        // Given
        every { getAlbumPhotosUseCase(ALBUM_ID) } returns flowOf(GetAlbumPhotosUseCase.Result.Empty)
        val sut = newSut()

        // When
        sut.onFavoriteClick(photoId = 7)

        // Then
        coVerify(exactly = 1) { toggleAlbumFavoriteUseCase(7) }
    }

    @Test
    fun `emits favoriteFailed when the favorite cannot be saved`() = runTest {
        // Given
        every { getAlbumPhotosUseCase(ALBUM_ID) } returns flowOf(GetAlbumPhotosUseCase.Result.Empty)
        coEvery { toggleAlbumFavoriteUseCase(7) } throws DataUnavailableException()
        val sut = newSut()

        sut.favoriteFailed.test {
            // When
            sut.onFavoriteClick(photoId = 7)

            // Then
            assertEquals(Unit, awaitItem())
        }
    }

    private fun photo(id: Int) = Album(id = id, albumId = ALBUM_ID, title = "title $id", url = "url", thumbnailUrl = "thumb")

    private companion object {
        const val ALBUM_ID = 3
    }
}
