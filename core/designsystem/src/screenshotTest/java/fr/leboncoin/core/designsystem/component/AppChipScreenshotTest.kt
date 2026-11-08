package fr.leboncoin.core.designsystem.component

import androidx.compose.ui.test.hasText
import com.github.takahirom.roborazzi.captureRoboImage
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.theme.AppTheme
import org.junit.Test

class AppChipScreenshotTest : UiTest() {

    @Test
    fun appChip_screenshot() {
        composeTestRule.setContent { AppTheme { AppChip(text = "Album #1") } }

        composeTestRule.onNode(hasText("Album #1")).captureRoboImage()
    }
}
