package fr.leboncoin.core.ui

import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.model.Album
import org.junit.Test

class AlbumItemScreenshotTest : UiTest() {

    // Empty image URLs to facilitate test as Robolectric does no networking
    private val album = Album(
        id = 7,
        albumId = 3,
        title = "accusamus beatae ad facilis cum similique qui sunt",
        url = "",
        thumbnailUrl = "",
    )

    @Test
    fun albumItem_screenshot_default() {
        composeTestRule.setContent { AppTheme { AlbumItem(album = album) } }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun albumItem_screenshot_withFavoriteButton() {
        composeTestRule.setContent { AppTheme { AlbumItem(album = album, onFavoriteClick = {}) } }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun albumItem_screenshot_favorite() {
        composeTestRule.setContent {
            AppTheme { AlbumItem(album = album.copy(isFavorite = true), onFavoriteClick = {}) }
        }

        composeTestRule.onRoot().captureRoboImage()
    }
}
