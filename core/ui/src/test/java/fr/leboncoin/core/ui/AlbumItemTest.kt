package fr.leboncoin.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performClick
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.testing.hasLocalizedContentDescription
import fr.leboncoin.core.designsystem.testing.hasLocalizedText
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.model.Album
import org.junit.Assert.assertEquals
import org.junit.Test

class AlbumItemTest : UiTest() {

    private val album = Album(id = 7, albumId = 3, title = "accusamus beatae", url = "", thumbnailUrl = "")

    @Test
    fun albumItem_showsTitleAndChips() {
        composeTestRule.setContent { AppTheme { AlbumItem(album = album) } }

        composeTestRule.onNode(hasText("accusamus beatae")).assertIsDisplayed()
        composeTestRule.onNode(hasLocalizedText(R.string.core_ui_album_chip, 3)).assertIsDisplayed()
        composeTestRule.onNode(hasLocalizedText(R.string.core_ui_track_chip, 7)).assertIsDisplayed()
    }


    @Test
    fun albumItem_whenNoFavoriteCallback_favoriteButtonIsNotComposed() {
        composeTestRule.setContent { AppTheme { AlbumItem(album = album, onFavoriteClick = null) } }

        composeTestRule.onNode(hasLocalizedContentDescription(R.string.core_ui_favorite_add)).assertDoesNotExist()
    }

    @Test
    fun albumItem_whenNoSelectionCallback_isNotClickable() {
        composeTestRule.setContent { AppTheme { AlbumItem(album = album, onItemSelected = null) } }

        composeTestRule.onNode(hasText("accusamus beatae") and hasClickAction()).assertDoesNotExist()
    }

    @Test
    fun albumItem_whenFavoriteClicked_triggersCallbackWithAlbum() {
        // Given
        var clicked: Album? = null
        composeTestRule.setContent { AppTheme { AlbumItem(album = album, onFavoriteClick = { clicked = it }) } }

        // When
        composeTestRule
            .onNode(hasLocalizedContentDescription(R.string.core_ui_favorite_add) and hasClickAction())
            .performClick()

        // Then
        assertEquals(album, clicked)
    }

    @Test
    fun albumItem_whenFavorite_offersToRemoveIt() {
        composeTestRule.setContent {
            AppTheme { AlbumItem(album = album.copy(isFavorite = true), onFavoriteClick = {}) }
        }

        composeTestRule.onNode(hasLocalizedContentDescription(R.string.core_ui_favorite_remove)).assertIsDisplayed()
    }

    @Test
    fun albumItem_whenClicked_triggersSelectionCallbackWithAlbum() {
        // Given
        var selected: Album? = null
        composeTestRule.setContent { AppTheme { AlbumItem(album = album, onItemSelected = { selected = it }) } }

        // When
        composeTestRule.onNode(hasText("accusamus beatae") and hasClickAction()).performClick()

        // Then
        assertEquals(album, selected)
    }
}
