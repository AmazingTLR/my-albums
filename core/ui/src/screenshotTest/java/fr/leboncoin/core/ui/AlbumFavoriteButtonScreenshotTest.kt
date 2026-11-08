package fr.leboncoin.core.ui

import androidx.compose.ui.test.hasContentDescription
import com.github.takahirom.roborazzi.captureRoboImage
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.theme.AppTheme
import org.junit.Test

class AlbumFavoriteButtonScreenshotTest : UiTest() {

    @Test
    fun albumFavoriteButton_screenshot_favorite() {
        composeTestRule.setContent { AppTheme { AlbumFavoriteButton(isFavorite = true, onClick = {}) } }

        composeTestRule.onNode(hasContentDescription("Remove from favorites")).captureRoboImage()
    }

    @Test
    fun albumFavoriteButton_screenshot_notFavorite() {
        composeTestRule.setContent { AppTheme { AlbumFavoriteButton(isFavorite = false, onClick = {}) } }

        composeTestRule.onNode(hasContentDescription("Add to favorites")).captureRoboImage()
    }
}
