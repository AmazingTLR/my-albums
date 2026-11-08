package fr.leboncoin.core.designsystem.screen

import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.theme.AppTheme
import org.junit.Test

class ErrorScreenScreenshotTest : UiTest() {

    @Test
    fun errorScreen_screenshot() {
        composeTestRule.setContent { AppTheme { ErrorScreen(errorMessage = "Failed to load albums") } }

        composeTestRule.onRoot().captureRoboImage()
    }
}
