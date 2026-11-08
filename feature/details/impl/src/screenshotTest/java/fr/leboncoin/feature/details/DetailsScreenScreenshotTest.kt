package fr.leboncoin.feature.details

import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.model.Album
import org.junit.Test

class DetailsScreenScreenshotTest : UiTest() {

    // Empty image URLs to facilitate test as Robolectric does no networking
    private val photos = listOf(
        Album(id = 1, albumId = 1, title = "accusamus beatae ad facilis", url = "", thumbnailUrl = ""),
        Album(id = 2, albumId = 1, title = "reprehenderit est deserunt", url = "", thumbnailUrl = "", isFavorite = true),
    )

    @Test
    fun detailsScreen_loading_screenshot() {
        capture(DetailsUiState.Loading)
    }

    @Test
    fun detailsScreen_empty_screenshot() {
        capture(DetailsUiState.Empty)
    }

    @Test
    fun detailsScreen_loadFailed_screenshot() {
        capture(DetailsUiState.LoadFailed)
    }

    @Test
    fun detailsScreen_success_screenshot() {
        capture(DetailsUiState.Success(photos))
    }

    private fun capture(uiState: DetailsUiState) {
        // The loading spinner animates forever: freeze the clock so the captured frame is stable.
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent { AppTheme { DetailsScreen(uiState = uiState, onFavoriteClick = {}) } }
        composeTestRule.onRoot().captureRoboImage()
    }
}
