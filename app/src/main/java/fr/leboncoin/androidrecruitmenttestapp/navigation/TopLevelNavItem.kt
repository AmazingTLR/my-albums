package fr.leboncoin.androidrecruitmenttestapp.navigation

import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import fr.leboncoin.core.designsystem.component.AppIcon
import fr.leboncoin.core.designsystem.component.AppIcons
import fr.leboncoin.feature.albums.AlbumsNavKey
import fr.leboncoin.feature.albums.api.R as AlbumsApiR
import fr.leboncoin.feature.favorites.FavoritesNavKey
import fr.leboncoin.feature.favorites.api.R as FavoritesApiR
import fr.leboncoin.feature.settings.SettingsNavKey
import fr.leboncoin.feature.settings.api.R as SettingsApiR

/** A tab of the bottom bar. The title string lives in the feature `api` module so `:app` needs no `impl` resource. */
data class TopLevelNavItem(
    val icon: AppIcon,
    val selectedIcon: AppIcon,
    @StringRes val titleTextId: Int,
)

/** Insertion order is the tab order; the first entry is the start destination. */
val TopLevelNavItems: Map<NavKey, TopLevelNavItem> = linkedMapOf(
    AlbumsNavKey to TopLevelNavItem(
        icon = AppIcons.ImageOutlined,
        selectedIcon = AppIcons.ImageFilled,
        titleTextId = AlbumsApiR.string.feature_albums_api_title,
    ),
    FavoritesNavKey to TopLevelNavItem(
        icon = AppIcons.HeartOutlined,
        selectedIcon = AppIcons.HeartFilled,
        titleTextId = FavoritesApiR.string.feature_favorites_api_title,
    ),
    SettingsNavKey to TopLevelNavItem(
        icon = AppIcons.GearOutlined,
        selectedIcon = AppIcons.GearFilled,
        titleTextId = SettingsApiR.string.feature_settings_api_title,
    ),
)
