package fr.leboncoin.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.performClick
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.testing.hasLocalizedQuantityText
import fr.leboncoin.core.designsystem.testing.hasLocalizedText
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.model.AlbumGroup
import org.junit.Assert.assertEquals
import org.junit.Test

class AlbumGroupItemTest : UiTest() {

    private fun group(photoCount: Int) =
        AlbumGroup(albumId = 4, photoCount = photoCount, coverUrls = listOf("", ""))

    @Test
    fun albumGroupItem_showsTitleAndPluralPhotoCount() {
        composeTestRule.setContent { AppTheme { AlbumGroupItem(group = group(photoCount = 50), onItemSelected = {}) } }

        composeTestRule.onNode(hasLocalizedText(R.string.core_ui_album_group_title, 4)).assertIsDisplayed()
        composeTestRule
            .onNode(hasLocalizedQuantityText(R.plurals.core_ui_album_group_photo_count, 50, 50))
            .assertIsDisplayed()
    }

    @Test
    fun albumGroupItem_withASinglePhoto_usesTheSingularForm() {
        composeTestRule.setContent { AppTheme { AlbumGroupItem(group = group(photoCount = 1), onItemSelected = {}) } }

        composeTestRule
            .onNode(hasLocalizedQuantityText(R.plurals.core_ui_album_group_photo_count, 1, 1))
            .assertIsDisplayed()
    }

    @Test
    fun albumGroupItem_whenClicked_triggersCallbackWithAlbumId() {
        // Given
        var selectedAlbumId: Int? = null
        composeTestRule.setContent {
            AppTheme { AlbumGroupItem(group = group(photoCount = 2), onItemSelected = { selectedAlbumId = it }) }
        }

        // When
        composeTestRule.onNode(hasLocalizedText(R.string.core_ui_album_group_title, 4) and hasClickAction())
            .performClick()

        // Then
        assertEquals(4, selectedAlbumId)
    }

    @Test
    fun albumGroupItem_whenPhotoCountChipClicked_triggersCallbackWithAlbumId() {
        // Given
        var selectedAlbumId: Int? = null
        composeTestRule.setContent {
            AppTheme { AlbumGroupItem(group = group(photoCount = 50), onItemSelected = { selectedAlbumId = it }) }
        }

        // When: the chip is a button of its own, a tap on it must still select the card
        composeTestRule
            .onNode(hasLocalizedQuantityText(R.plurals.core_ui_album_group_photo_count, 50, 50))
            .performClick()

        // Then
        assertEquals(4, selectedAlbumId)
    }
}
