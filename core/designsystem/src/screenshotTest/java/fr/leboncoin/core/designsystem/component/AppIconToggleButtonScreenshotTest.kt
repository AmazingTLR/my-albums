package fr.leboncoin.core.designsystem.component

import androidx.compose.ui.test.hasContentDescription
import com.github.takahirom.roborazzi.captureRoboImage
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.theme.AppTheme
import org.junit.Test

class AppIconToggleButtonScreenshotTest : UiTest() {

    @Test
    fun appIconToggleButton_screenshot_checked() {
        setButton(checked = true)

        composeTestRule.onNode(hasContentDescription("Favorite")).captureRoboImage()
    }

    @Test
    fun appIconToggleButton_screenshot_unchecked() {
        setButton(checked = false)

        composeTestRule.onNode(hasContentDescription("Favorite")).captureRoboImage()
    }

    private fun setButton(checked: Boolean) {
        composeTestRule.setContent {
            AppTheme {
                AppIconToggleButton(
                    checked = checked,
                    onCheckedChange = {},
                    checkedIcon = AppIcons.HeartFilled,
                    uncheckedIcon = AppIcons.HeartOutlined,
                    contentDescription = "Favorite",
                )
            }
        }
    }
}
