package fr.leboncoin.androidrecruitmenttestapp.util

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.core.util.Consumer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged

val Configuration.isSystemInDarkTheme: Boolean
    get() = (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

fun ComponentActivity.systemDarkThemeFlow(): Flow<Boolean> = callbackFlow {
    trySend(resources.configuration.isSystemInDarkTheme)
    val listener = Consumer<Configuration> { trySend(it.isSystemInDarkTheme) }
    addOnConfigurationChangedListener(listener)
    awaitClose { removeOnConfigurationChangedListener(listener) }
}
    .distinctUntilChanged()
    .conflate()
