package fr.leboncoin.feature.favorites

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performClick
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.testing.hasLocalizedContentDescription
import fr.leboncoin.core.designsystem.testing.hasLocalizedText
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.model.Album
import org.junit.Assert.assertEquals
import org.junit.Test
import fr.leboncoin.core.ui.R as UiR
import fr.leboncoin.feature.favorites.impl.R as FavoritesR

class FavoritesScreenTest : UiTest() {

    private val albums = listOf(
        Album(id = 1, albumId = 1, title = "first favorite", url = "", thumbnailUrl = "", isFavorite = true),
        Album(id = 2, albumId = 1, title = "second favorite", url = "", thumbnailUrl = "", isFavorite = true),
    )

    @Test
    fun favoritesScreen_empty_showsTheEmptyMessage() {
        setScreen(FavoritesUiState.Empty)

        composeTestRule.onNode(hasLocalizedText(FavoritesR.string.feature_favorites_impl_empty)).assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_loadFailed_showsTheErrorMessage() {
        setScreen(FavoritesUiState.LoadFailed)

        composeTestRule
            .onNode(hasLocalizedText(FavoritesR.string.feature_favorites_impl_load_failed))
            .assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_success_showsEveryFavorite() {
        setScreen(FavoritesUiState.Success(albums))

        composeTestRule.onNode(hasText("first favorite")).assertIsDisplayed()
        composeTestRule.onNode(hasText("second favorite")).assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_whenHeartClicked_triggersCallbackWithTheAlbum() {
        // Given
        var clicked: Album? = null
        setScreen(FavoritesUiState.Success(listOf(albums.first())), onFavoriteClick = { clicked = it })

        // When
        composeTestRule
            .onNode(hasLocalizedContentDescription(UiR.string.core_ui_favorite_remove) and hasClickAction())
            .performClick()

        // Then
        assertEquals(albums.first(), clicked)
    }

    private fun setScreen(uiState: FavoritesUiState, onFavoriteClick: (Album) -> Unit = {}) {
        composeTestRule.setContent {
            AppTheme { FavoritesScreen(uiState = uiState, onFavoriteClick = onFavoriteClick) }
        }
    }
}
