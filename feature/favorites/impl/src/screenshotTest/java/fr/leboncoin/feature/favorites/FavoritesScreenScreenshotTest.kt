package fr.leboncoin.feature.favorites

import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.model.Album
import org.junit.Test

class FavoritesScreenScreenshotTest : UiTest() {

    // Empty image URLs: Robolectric does no network I/O, images render as placeholders.
    private val albums = listOf(
        Album(id = 1, albumId = 1, title = "accusamus beatae ad facilis", url = "", thumbnailUrl = "", isFavorite = true),
        Album(id = 2, albumId = 1, title = "reprehenderit est deserunt", url = "", thumbnailUrl = "", isFavorite = true),
    )

    @Test
    fun favoritesScreen_loading_screenshot() {
        capture(FavoritesUiState.Loading)
    }

    @Test
    fun favoritesScreen_empty_screenshot() {
        capture(FavoritesUiState.Empty)
    }

    @Test
    fun favoritesScreen_loadFailed_screenshot() {
        capture(FavoritesUiState.LoadFailed)
    }

    @Test
    fun favoritesScreen_success_screenshot() {
        capture(FavoritesUiState.Success(albums))
    }

    private fun capture(uiState: FavoritesUiState) {
        // The loading spinner animates forever: freeze the clock so the captured frame is stable.
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent { AppTheme { FavoritesScreen(uiState = uiState, onFavoriteClick = {}) } }
        composeTestRule.onRoot().captureRoboImage()
    }
}
