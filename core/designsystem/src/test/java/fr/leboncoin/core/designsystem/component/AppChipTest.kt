package fr.leboncoin.core.designsystem.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performClick
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Test

class AppChipTest : UiTest() {

    @Test
    fun appChip_showsItsText() {
        composeTestRule.setContent { AppTheme { AppChip(text = "Album #1") } }

        composeTestRule.onNode(hasText("Album #1")).assertIsDisplayed()
    }

    // Interactions

    @Test
    fun appChip_insideClickableCard_whenClicked_triggersTheCardCallback() {
        // Given
        var cardClicks = 0
        composeTestRule.setContent {
            AppTheme { AppCard(onClick = { cardClicks++ }) { AppChip(text = "Album #1") } }
        }

        // When
        composeTestRule.onNode(hasText("Album #1")).performClick()

        // Then
        assertEquals(1, cardClicks)
    }

    @Test
    fun appChip_insideNonClickableCardNestedInAClickableCard_doesNotTriggerTheOuterCallback() {
        // Given
        var outerClicks = 0
        composeTestRule.setContent {
            AppTheme {
                AppCard(onClick = { outerClicks++ }) {
                    AppCard { AppChip(text = "Album #1") }
                }
            }
        }

        // When
        composeTestRule.onNode(hasText("Album #1")).performClick()

        // Then
        assertEquals(0, outerClicks)
    }
}
