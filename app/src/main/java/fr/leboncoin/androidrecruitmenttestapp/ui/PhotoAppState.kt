package fr.leboncoin.androidrecruitmenttestapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import fr.leboncoin.androidrecruitmenttestapp.util.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberPhotoAppState(networkMonitor: NetworkMonitor): PhotoAppState {
    val coroutineScope = rememberCoroutineScope()
    return remember(networkMonitor, coroutineScope) { PhotoAppState(networkMonitor, coroutineScope) }
}

class PhotoAppState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope,
) {
    // Starts at false so nothing flashes before the first real reading when the device is online.
    val isOffline: StateFlow<Boolean> = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )
}
