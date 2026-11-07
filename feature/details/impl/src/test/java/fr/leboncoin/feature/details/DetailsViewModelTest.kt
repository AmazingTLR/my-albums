package fr.leboncoin.feature.details

import app.cash.turbine.test
import fr.leboncoin.core.common.exception.DataUnavailableException
import fr.leboncoin.core.model.Album
import fr.leboncoin.core.testing.CoroutinesRule
import fr.leboncoin.core.usecase.GetAlbumPhotosUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DetailsViewModelTest {

    @get:Rule
    val coroutinesRule = CoroutinesRule()

    private val getAlbumPhotosUseCase = mockk<GetAlbumPhotosUseCase>()

    private fun newSut() = DetailsViewModel(albumId = ALBUM_ID, getAlbumPhotosUseCase = getAlbumPhotosUseCase)

    @Test
    fun `emits Success with the album photos once loaded`() = runTest {
        // Given
        val photos = listOf(photo(1), photo(2))
        coEvery { getAlbumPhotosUseCase(ALBUM_ID) } returns GetAlbumPhotosUseCase.Result.Success(photos)

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(DetailsUiState.Success(photos), expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { getAlbumPhotosUseCase(ALBUM_ID) }
    }

    @Test
    fun `emits Empty when the album has no photo`() = runTest {
        // Given
        coEvery { getAlbumPhotosUseCase(ALBUM_ID) } returns GetAlbumPhotosUseCase.Result.Empty

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
        coEvery { getAlbumPhotosUseCase(ALBUM_ID) } throws DataUnavailableException()

        // When
        val sut = newSut()

        // Then
        sut.uiState.test {
            assertEquals(DetailsUiState.LoadFailed, expectMostRecentItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun photo(id: Int) = Album(id = id, albumId = ALBUM_ID, title = "title $id", url = "url", thumbnailUrl = "thumb")

    private companion object {
        const val ALBUM_ID = 3
    }
}
