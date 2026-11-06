package fr.leboncoin.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import fr.leboncoin.core.designsystem.component.AppCard
import fr.leboncoin.core.designsystem.component.AppChip
import fr.leboncoin.core.designsystem.component.AppSurface
import fr.leboncoin.core.designsystem.component.AppText
import fr.leboncoin.core.designsystem.theme.AppTheme
import fr.leboncoin.core.designsystem.theme.ThemePreviews
import fr.leboncoin.core.model.Album

private const val IMAGE_USER_AGENT = "LeboncoinApp/1.0"

@Composable
fun AlbumItem(
    album: Album,
    onItemSelected: (Album) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = AppTheme.dimensions
    AppCard(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensions.albumItemHeight)
            .padding(horizontal = dimensions.spaceMedium),
        onClick = { onItemSelected(album) },
    ) {
        Row {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(album.thumbnailUrl)
                    .httpHeaders(
                        NetworkHeaders.Builder()
                            .add("User-Agent", IMAGE_USER_AGENT)
                            .build(),
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = album.title,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensions.spaceMediumSmall),
            ) {
                AppText(
                    text = album.title,
                    style = AppTheme.typography.caption,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensions.spaceSmall),
                ) {
                    AppChip(text = stringResource(R.string.core_ui_album_chip, album.albumId))
                    AppChip(text = stringResource(R.string.core_ui_track_chip, album.id))
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun AlbumItemPreview() {
    AppTheme {
        AppSurface {
            AlbumItem(
                album = Album(
                    id = 1,
                    albumId = 1,
                    title = "accusamus beatae ad facilis cum similique qui sunt",
                    url = "",
                    thumbnailUrl = "",
                ),
                onItemSelected = {},
            )
        }
    }
}
