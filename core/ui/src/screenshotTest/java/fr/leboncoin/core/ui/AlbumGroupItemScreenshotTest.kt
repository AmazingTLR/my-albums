package fr.leboncoin.core.ui

import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.model.AlbumGroup
import org.junit.Test

class AlbumGroupItemScreenshotTest : UiTest() {

    @Test
    fun albumGroupItem_screenshot_fullCovers() {
        val group = AlbumGroup(albumId = 1, photoCount = 50, coverUrls = List(4) { "" })
        composeTestRule.setContent { AppTheme { AlbumGroupItem(group = group, onItemSelected = {}) } }

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun albumGroupItem_screenshot_singlePhoto() {
        val group = AlbumGroup(albumId = 2, photoCount = 1, coverUrls = listOf(""))
        composeTestRule.setContent { AppTheme { AlbumGroupItem(group = group, onItemSelected = {}) } }

        composeTestRule.onRoot().captureRoboImage()
    }
}
