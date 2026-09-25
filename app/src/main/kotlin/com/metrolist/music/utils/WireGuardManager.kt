package com.metrolist.music.utils

import android.content.Context
import android.widget.Toast
import com.metrolist.innertube.YouTube
import com.metrolist.music.R
import timber.log.Timber
import java.net.InetSocketAddress
import java.net.Proxy

object WireGuardManager {
    private const val DEFAULT_SOCKS_PORT = 25344

    var isRunning = false
        private set
    var currentPort: Int = DEFAULT_SOCKS_PORT
        private set

    fun start(context: Context, configName: String, configContent: String): Boolean {
        if (isRunning) {
            Timber.w("WireGuard proxy already running")
            return true
        }

        val config = WireGuardConfig.parse(configName, configContent)
        if (config == null) {
            Toast.makeText(context, context.getString(R.string.wireguard_config_error), Toast.LENGTH_SHORT).show()
            return false
        }

        if (!WireGuardNative.loadLibrary()) {
            Toast.makeText(context, "wireproxy native library not found", Toast.LENGTH_LONG).show()
            return false
        }

        val quickConfig = buildQuickConfig(configContent, DEFAULT_SOCKS_PORT)

        return try {
            val result = WireGuardNative.startProxy(quickConfig, DEFAULT_SOCKS_PORT)
            if (result == 0) {
                isRunning = true
                currentPort = DEFAULT_SOCKS_PORT
                YouTube.proxy = Proxy(Proxy.Type.SOCKS, InetSocketAddress("127.0.0.1", DEFAULT_SOCKS_PORT))
                Timber.i("WireGuard proxy started on port $DEFAULT_SOCKS_PORT")
                true
            } else {
                Timber.e("WireGuard proxy start failed with code $result")
                Toast.makeText(context, "Failed to start WireGuard proxy (code: $result)", Toast.LENGTH_SHORT).show()
                false
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to start WireGuard proxy")
            Toast.makeText(context, "Failed to start WireGuard proxy: ${e.message}", Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun stop() {
        if (!isRunning) return
        try {
            WireGuardNative.stopProxy()
            YouTube.proxy = null
            YouTube.proxyAuth = null
            isRunning = false
            Timber.i("WireGuard proxy stopped")
        } catch (e: Exception) {
            Timber.e(e, "Failed to stop WireGuard proxy")
        }
    }

    private fun buildQuickConfig(configContent: String, socksPort: Int): String = buildString {
        append(configContent.trimEnd())
        append('\n')
        append("[Socks5]\n")
        append("BindAddress = 127.0.0.1:$socksPort\n")
    }
}
