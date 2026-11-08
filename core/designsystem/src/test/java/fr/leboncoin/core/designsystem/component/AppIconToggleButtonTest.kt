package fr.leboncoin.core.designsystem.component

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.performClick
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Test

class AppIconToggleButtonTest : UiTest() {

    @Test
    fun appIconToggleButton_whenUnchecked_clickRequestsChecked() {
        // Given
        var requested: Boolean? = null
        setButton(checked = false, onCheckedChange = { requested = it })

        // When
        composeTestRule.onNode(hasContentDescription("Favorite") and hasClickAction()).performClick()

        // Then
        assertEquals(true, requested)
    }

    @Test
    fun appIconToggleButton_whenChecked_clickRequestsUnchecked() {
        // Given
        var requested: Boolean? = null
        setButton(checked = true, onCheckedChange = { requested = it })

        // When
        composeTestRule.onNode(hasContentDescription("Favorite") and hasClickAction()).performClick()

        // Then
        assertEquals(false, requested)
    }

    private fun setButton(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
        composeTestRule.setContent {
            AppTheme {
                AppIconToggleButton(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    checkedIcon = AppIcons.HeartFilled,
                    uncheckedIcon = AppIcons.HeartOutlined,
                    contentDescription = "Favorite",
                )
            }
        }
    }
}
