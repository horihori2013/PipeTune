package com.metrolist.music.utils

import timber.log.Timber

data class WireGuardPeer(
    val publicKey: String,
    val endpoint: String? = null,
    val allowedIPs: List<String> = emptyList(),
    val persistentKeepalive: Int? = null,
    val preSharedKey: String? = null,
)

data class WireGuardConfig(
    val name: String,
    val privateKey: String,
    val address: List<String>,
    val dns: List<String> = emptyList(),
    val mtu: Int? = null,
    val peers: List<WireGuardPeer> = emptyList(),
) {
    val endpoint: String?
        get() = peers.firstOrNull { it.endpoint != null }?.endpoint

    val endpointHost: String?
        get() = endpoint?.substringBeforeLast(":")

    val endpointPort: Int
        get() {
            val ep = endpoint ?: return 51820
            val lastColon = ep.lastIndexOf(":")
            return if (lastColon > 0) {
                ep.substring(lastColon + 1).toIntOrNull() ?: 51820
            } else {
                51820
            }
        }

    companion object {
        fun parse(name: String, configText: String): WireGuardConfig? {
            return try {
                var privateKey = ""
                val address = mutableListOf<String>()
                val dns = mutableListOf<String>()
                var mtu: Int? = null
                val peers = mutableListOf<WireGuardPeer>()
                var currentPeerBuilder = WireGuardPeerBuilder()
                var inInterface = false
                var inPeer = false

                configText.lines().forEach { rawLine ->
                    val line = rawLine.trim()
                    if (line.isBlank() || line.startsWith("#")) return@forEach

                    when {
                        line == "[Interface]" -> {
                            inInterface = true
                            inPeer = false
                        }
                        line == "[Peer]" -> {
                            if (inPeer && currentPeerBuilder.publicKey.isNotEmpty()) {
                                peers.add(currentPeerBuilder.build())
                            }
                            inInterface = false
                            inPeer = true
                            currentPeerBuilder = WireGuardPeerBuilder()
                        }
                        line.contains("=") -> {
                            val eqIdx = line.indexOf("=")
                            val key = line.substring(0, eqIdx).trim()
                            val value = line.substring(eqIdx + 1).trim()

                            when {
                                inInterface -> when (key) {
                                    "PrivateKey" -> privateKey = value
                                    "Address" -> address.addAll(value.split(",").map { it.trim() })
                                    "DNS" -> dns.addAll(value.split(",").map { it.trim() })
                                    "MTU" -> mtu = value.toIntOrNull()
                                }
                                inPeer -> when (key) {
                                    "PublicKey" -> currentPeerBuilder.publicKey = value
                                    "Endpoint" -> currentPeerBuilder.endpoint = value
                                    "AllowedIPs" -> currentPeerBuilder.allowedIPs = value.split(",").map { it.trim() }
                                    "PersistentKeepalive" -> currentPeerBuilder.persistentKeepalive = value.toIntOrNull()
                                    "PresharedKey" -> currentPeerBuilder.preSharedKey = value
                                }
                            }
                        }
                    }
                }

                if (inPeer && currentPeerBuilder.publicKey.isNotEmpty()) {
                    peers.add(currentPeerBuilder.build())
                }

                if (privateKey.isEmpty()) {
                    Timber.e("WireGuard config missing PrivateKey")
                    return null
                }

                if (address.isEmpty()) {
                    Timber.e("WireGuard config missing Address")
                    return null
                }

                WireGuardConfig(
                    name = name,
                    privateKey = privateKey,
                    address = address,
                    dns = dns,
                    mtu = mtu,
                    peers = peers,
                )
            } catch (e: Exception) {
                Timber.e(e, "Failed to parse WireGuard config")
                null
            }
        }
    }

    private class WireGuardPeerBuilder {
        var publicKey: String = ""
        var endpoint: String? = null
        var allowedIPs: List<String> = emptyList()
        var persistentKeepalive: Int? = null
        var preSharedKey: String? = null

        fun build() = WireGuardPeer(
            publicKey = publicKey,
            endpoint = endpoint,
            allowedIPs = allowedIPs,
            persistentKeepalive = persistentKeepalive,
            preSharedKey = preSharedKey,
        )
    }
}
