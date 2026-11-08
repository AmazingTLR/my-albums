package fr.leboncoin.feature.albums

import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.model.AlbumGroup
import org.junit.Test

/** Record reference screenshots: `./gradlew :feature:albums:impl:recordRoborazziDebug` */
class AlbumsScreenScreenshotTest : UiTest() {

    // Empty image URLs: Robolectric does no network I/O, images render as placeholders.
    private val groups = listOf(
        AlbumGroup(albumId = 1, photoCount = 50, coverUrls = List(4) { "" }),
        AlbumGroup(albumId = 2, photoCount = 1, coverUrls = listOf("")),
    )

    @Test
    fun albumsScreen_loading_screenshot() {
        capture(AlbumsUiState.Loading)
    }

    @Test
    fun albumsScreen_loadFailed_screenshot() {
        capture(AlbumsUiState.LoadFailed)
    }

    @Test
    fun albumsScreen_success_screenshot() {
        capture(AlbumsUiState.Success(groups))
    }

    private fun capture(uiState: AlbumsUiState) {
        // The loading spinner animates forever: freeze the clock so the captured frame is stable.
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent { AppTheme { AlbumsScreen(uiState = uiState, onAlbumClick = {}) } }
        composeTestRule.onRoot().captureRoboImage()
    }
}
