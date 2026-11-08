package fr.leboncoin.feature.albums

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.performClick
import fr.leboncoin.core.designsystem.testing.UiTest
import fr.leboncoin.core.designsystem.testing.hasLocalizedText
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.model.AlbumGroup
import org.junit.Assert.assertEquals
import org.junit.Test
import fr.leboncoin.core.ui.R as UiR
import fr.leboncoin.feature.albums.impl.R as AlbumsR

class AlbumsScreenTest : UiTest() {

    private val groups = listOf(
        AlbumGroup(albumId = 1, photoCount = 50, coverUrls = listOf("", "")),
        AlbumGroup(albumId = 2, photoCount = 1, coverUrls = listOf("")),
    )

    @Test
    fun albumsScreen_loadFailed_showsTheErrorMessage() {
        setScreen(AlbumsUiState.LoadFailed)

        composeTestRule.onNode(hasLocalizedText(AlbumsR.string.feature_albums_impl_load_failed)).assertIsDisplayed()
    }

    @Test
    fun albumsScreen_success_showsEveryAlbumGroup() {
        setScreen(AlbumsUiState.Success(groups))

        composeTestRule.onNode(hasLocalizedText(UiR.string.core_ui_album_group_title, 1)).assertIsDisplayed()
        composeTestRule.onNode(hasLocalizedText(UiR.string.core_ui_album_group_title, 2)).assertIsDisplayed()
    }

    @Test
    fun albumsScreen_whenAlbumClicked_triggersCallbackWithAlbumId() {
        // Given
        var clickedAlbumId: Int? = null
        setScreen(AlbumsUiState.Success(groups), onAlbumClick = { clickedAlbumId = it })

        // When
        composeTestRule
            .onNode(hasLocalizedText(UiR.string.core_ui_album_group_title, 2) and hasClickAction())
            .performClick()

        // Then
        assertEquals(2, clickedAlbumId)
    }

    private fun setScreen(uiState: AlbumsUiState, onAlbumClick: (Int) -> Unit = {}) {
        composeTestRule.setContent { AppTheme { AlbumsScreen(uiState = uiState, onAlbumClick = onAlbumClick, onRefresh = {}) } }
    }
}
