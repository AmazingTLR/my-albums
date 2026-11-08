package fr.leboncoin.feature.details

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
import fr.leboncoin.core.ui.R as UiR
import fr.leboncoin.feature.details.impl.R as DetailsR

class DetailsScreenTest : UiTest() {

    private val photo = Album(id = 1, albumId = 1, title = "accusamus beatae", url = "", thumbnailUrl = "")
    private val favoritePhoto =
        Album(id = 2, albumId = 1, title = "reprehenderit est", url = "", thumbnailUrl = "", isFavorite = true)

    @Test
    fun detailsScreen_empty_showsTheEmptyMessage() {
        setScreen(DetailsUiState.Empty)

        composeTestRule.onNode(hasLocalizedText(DetailsR.string.feature_details_impl_empty)).assertIsDisplayed()
    }

    @Test
    fun detailsScreen_loadFailed_showsTheErrorMessage() {
        setScreen(DetailsUiState.LoadFailed)

        composeTestRule.onNode(hasLocalizedText(DetailsR.string.feature_details_impl_load_failed)).assertIsDisplayed()
    }

    @Test
    fun detailsScreen_success_showsEveryPhoto() {
        setScreen(DetailsUiState.Success(listOf(photo, favoritePhoto)))

        composeTestRule.onNode(hasText("accusamus beatae")).assertIsDisplayed()
        composeTestRule.onNode(hasText("reprehenderit est")).assertIsDisplayed()
    }

    @Test
    fun detailsScreen_success_reflectsTheFavoriteStateOfEachPhoto() {
        setScreen(DetailsUiState.Success(listOf(photo, favoritePhoto)))

        composeTestRule.onNode(hasLocalizedContentDescription(UiR.string.core_ui_favorite_add)).assertIsDisplayed()
        composeTestRule.onNode(hasLocalizedContentDescription(UiR.string.core_ui_favorite_remove)).assertIsDisplayed()
    }


    @Test
    fun detailsScreen_whenHeartClicked_triggersCallbackWithThePhoto() {
        // Given
        var clicked: Album? = null
        setScreen(DetailsUiState.Success(listOf(photo)), onFavoriteClick = { clicked = it })

        // When
        composeTestRule
            .onNode(hasLocalizedContentDescription(UiR.string.core_ui_favorite_add) and hasClickAction())
            .performClick()

        // Then
        assertEquals(photo, clicked)
    }

    private fun setScreen(uiState: DetailsUiState, onFavoriteClick: (Album) -> Unit = {}) {
        composeTestRule.setContent {
            AppTheme { DetailsScreen(uiState = uiState, onFavoriteClick = onFavoriteClick) }
        }
    }
}
