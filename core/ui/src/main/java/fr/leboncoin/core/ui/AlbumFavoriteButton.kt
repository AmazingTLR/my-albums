package fr.leboncoin.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import fr.leboncoin.core.designsystem.component.AppIconToggleButton
import fr.leboncoin.core.designsystem.component.AppIcons

@Composable
fun AlbumFavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppIconToggleButton(
        checked = isFavorite,
        onCheckedChange = { onClick() },
        checkedIcon = AppIcons.HeartFilled,
        uncheckedIcon = AppIcons.HeartOutlined,
        contentDescription = stringResource(
            if (isFavorite) R.string.core_ui_favorite_remove else R.string.core_ui_favorite_add,
        ),
        modifier = modifier,
    )
}
