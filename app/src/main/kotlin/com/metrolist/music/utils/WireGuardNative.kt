package com.metrolist.music.utils

import timber.log.Timber

object WireGuardNative {
    private var loaded = false

    fun loadLibrary(): Boolean {
        if (loaded) return true
        return try {
            System.loadLibrary("wireproxy")
            loaded = true
            Timber.i("wireproxy native library loaded")
            true
        } catch (e: UnsatisfiedLinkError) {
            Timber.e(e, "Failed to load wireproxy native library")
            false
        }
    }

    fun isAvailable(): Boolean = loaded

    external fun startProxy(config: String, socksPort: Int): Int
    external fun stopProxy()
    external fun getVersion(): String
}
