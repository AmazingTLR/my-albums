package fr.leboncoin.feature.favorites.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import fr.leboncoin.feature.favorites.FavoritesNavKey
import fr.leboncoin.feature.favorites.FavoritesScreen

fun EntryProviderScope<NavKey>.favoritesEntry() {
    entry<FavoritesNavKey> {
        FavoritesScreen()
    }
}
