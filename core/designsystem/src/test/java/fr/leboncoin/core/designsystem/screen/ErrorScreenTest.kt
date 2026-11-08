package fr.leboncoin.core.designsystem.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.theme.AppTheme
import org.junit.Test

class ErrorScreenTest : UiTest() {

    @Test
    fun errorScreen_showsTheErrorMessage() {
        composeTestRule.setContent { AppTheme { ErrorScreen(errorMessage = "Something went wrong") } }

        composeTestRule.onNode(hasText("Something went wrong")).assertIsDisplayed()
    }
}
