/**
 * Metrolist Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.metrolist.music.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import timber.log.Timber

/**
 * Binds the app process to the cellular network so all of its traffic is sent over
 * mobile data even when Wi-Fi is the system default network.
 */
object MobileDataNetwork {
    @Volatile
    private var callback: ConnectivityManager.NetworkCallback? = null

    fun setEnabled(context: Context, enabled: Boolean) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return

        if (enabled) {
            if (callback != null) return
            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    connectivityManager.bindProcessToNetwork(network)
                    Timber.d("Process bound to mobile data network")
                }

                override fun onLost(network: Network) {
                    connectivityManager.bindProcessToNetwork(null)
                    Timber.d("Mobile data network lost; process binding cleared")
                }

                override fun onUnavailable() {
                    connectivityManager.bindProcessToNetwork(null)
                    Timber.w("Mobile data network request unavailable")
                }
            }
            val request = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            try {
                connectivityManager.requestNetwork(request, networkCallback)
                callback = networkCallback
                Timber.d("Mobile data network requested")
            } catch (e: Exception) {
                Timber.e(e, "Failed to request mobile data network")
            }
        } else {
            callback?.let {
                runCatching { connectivityManager.unregisterNetworkCallback(it) }
            }
            callback = null
            connectivityManager.bindProcessToNetwork(null)
        }
    }
}
