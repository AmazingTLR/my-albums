package fr.leboncoin.feature.details.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import fr.leboncoin.feature.details.DetailsNavKey
import fr.leboncoin.feature.details.DetailsScreen

fun EntryProviderScope<NavKey>.detailsEntry() {
    entry<DetailsNavKey> { key ->
        DetailsScreen(albumId = key.albumId)
    }
}
