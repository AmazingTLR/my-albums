package fr.leboncoin.androidrecruitmenttestapp.util

import kotlinx.coroutines.flow.Flow

/** Reports whether the device has a network able to reach the internet. */
interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}
