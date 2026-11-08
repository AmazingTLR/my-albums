package fr.leboncoin.core.ui

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.performClick
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.testing.hasLocalizedContentDescription
import fr.leboncoin.core.designsystem.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Test

class AlbumFavoriteButtonTest : UiTest() {

    @Test
    fun albumFavoriteButton_whenNotFavorite_offersToAddIt() {
        composeTestRule.setContent { AppTheme { AlbumFavoriteButton(isFavorite = false, onClick = {}) } }

        composeTestRule.onNode(hasLocalizedContentDescription(R.string.core_ui_favorite_add)).assertExists()
        composeTestRule.onNode(hasLocalizedContentDescription(R.string.core_ui_favorite_remove)).assertDoesNotExist()
    }

    @Test
    fun albumFavoriteButton_whenFavorite_offersToRemoveIt() {
        composeTestRule.setContent { AppTheme { AlbumFavoriteButton(isFavorite = true, onClick = {}) } }

        composeTestRule.onNode(hasLocalizedContentDescription(R.string.core_ui_favorite_remove)).assertExists()
        composeTestRule.onNode(hasLocalizedContentDescription(R.string.core_ui_favorite_add)).assertDoesNotExist()
    }

    @Test
    fun albumFavoriteButton_whenClicked_triggersCallback() {
        // Given
        var clicks = 0
        composeTestRule.setContent { AppTheme { AlbumFavoriteButton(isFavorite = false, onClick = { clicks++ }) } }

        // When
        composeTestRule
            .onNode(hasLocalizedContentDescription(R.string.core_ui_favorite_add) and hasClickAction())
            .performClick()

        // Then
        assertEquals(1, clicks)
    }
}
