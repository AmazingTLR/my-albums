package fr.leboncoin.androidrecruitmenttestapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn

internal class ConnectivityManagerNetworkMonitor(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) : NetworkMonitor {

    override val isOnline: Flow<Boolean> = callbackFlow {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        if (connectivityManager == null) {
            channel.trySend(false)
            channel.close()
            return@callbackFlow
        }

        val callback = object : NetworkCallback() {
            // Every network able to reach the internet is tracked: losing Wi-Fi while mobile data
            // is still up must not be reported as offline.
            private val networks = mutableSetOf<Network>()

            override fun onAvailable(network: Network) {
                networks += network
                channel.trySend(true)
            }

            override fun onLost(network: Network) {
                networks -= network
                channel.trySend(networks.isNotEmpty())
            }
        }

        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().addCapability(NET_CAPABILITY_INTERNET).build(),
            callback,
        )
        channel.trySend(connectivityManager.isCurrentlyConnected())

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }
        .flowOn(ioDispatcher)
        .conflate()

    private fun ConnectivityManager.isCurrentlyConnected(): Boolean =
        activeNetwork
            ?.let(::getNetworkCapabilities)
            ?.hasCapability(NET_CAPABILITY_INTERNET)
            ?: false
}
