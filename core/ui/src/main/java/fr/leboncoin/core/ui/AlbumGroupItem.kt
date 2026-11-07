package fr.leboncoin.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import fr.leboncoin.core.designsystem.component.AppCard
import fr.leboncoin.core.designsystem.component.AppChip
import fr.leboncoin.core.designsystem.component.AppSurface
import fr.leboncoin.core.designsystem.component.AppText
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.designsystem.theme.ThemePreviews
import fr.leboncoin.core.model.AlbumGroup

@Composable
fun AlbumGroupItem(
    group: AlbumGroup,
    onItemSelected: (albumId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = AppTheme.dimensions
    AppCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.spaceMedium),
        onClick = { onItemSelected(group.albumId) },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensions.spaceSmall),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                AppText(
                    text = stringResource(R.string.core_ui_album_group_title, group.albumId),
                    style = AppTheme.typography.caption,
                )
                AppChip(
                    text = pluralStringResource(
                        R.plurals.core_ui_album_group_photo_count,
                        group.photoCount,
                        group.photoCount,
                    ),
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(dimensions.spaceSmall)) {
                // Fixed slots keep every card the same height, whatever the number of covers.
                repeat(AlbumGroup.DEFAULT_NUMBER_OF_COVERS_PER_GROUP) { index ->
                    val slotModifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                    val coverUrl = group.coverUrls.getOrNull(index)
                    if (coverUrl != null) {
                        AlbumThumbnail(url = coverUrl, contentDescription = null, modifier = slotModifier)
                    } else {
                        Spacer(slotModifier)
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun AlbumGroupItemPreview() {
    AppTheme {
        AppSurface {
            AlbumGroupItem(
                group = AlbumGroup(albumId = 1, photoCount = 50, coverUrls = listOf("", "", "")),
                onItemSelected = {},
            )
        }
    }
}
