package fr.leboncoin.core.designsystem.screen

import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.theme.AppTheme
import org.junit.Test

/** Record reference screenshots: `./gradlew :core:designsystem:recordRoborazziDebug` */
class LoadingScreenScreenshotTest : UiTest() {

    @Test
    fun loadingScreen_screenshot() {
        // The spinner animates forever: freeze the clock so the captured frame is always the same.
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent { AppTheme { LoadingScreen() } }

        composeTestRule.onRoot().captureRoboImage()
    }
}
