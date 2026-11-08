package fr.leboncoin.feature.settings.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import fr.leboncoin.feature.settings.SettingsNavKey
import fr.leboncoin.feature.settings.SettingsScreen

fun EntryProviderScope<NavKey>.settingsEntry() {
    entry<SettingsNavKey> {
        SettingsScreen()
    }
}
