package fr.leboncoin.feature.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.leboncoin.core.designsystem.component.AppIllustration
import fr.leboncoin.core.designsystem.component.AppSurface
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.designsystem.theme.ThemePreviews
import fr.leboncoin.core.ui.analytics.TrackScreenViewEvent
import fr.leboncoin.feature.details.impl.R

/** Placeholder: the real details screen is not implemented yet. */
@Composable
internal fun DetailsScreen(modifier: Modifier = Modifier) {
    TrackScreenViewEvent("DetailsScreen")
    AppIllustration(
        drawableRes = R.drawable.feature_details_impl_work_in_progress,
        contentDescription = null,
        modifier = modifier,
    )
}

@ThemePreviews
@Composable
private fun DetailsScreenPreview() {
    AppTheme {
        AppSurface {
            DetailsScreen()
        }
    }
}
