package com.example.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.TimeUnit

object NetworkPingHelper {
    private val client = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .build()

    suspend fun measurePingMs(host: String = "cloud.boosteroid.com", port: Int = 443): Long = withContext(Dispatchers.IO) {
        val start = System.currentTimeMillis()
        try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress(host, port), 2500)
            }
            val elapsed = System.currentTimeMillis() - start
            elapsed.coerceAtLeast(12L)
        } catch (e: Exception) {
            // Fallback via HTTP HEAD if socket raw fails
            try {
                val httpStart = System.currentTimeMillis()
                val request = Request.Builder()
                    .url("https://$host")
                    .head()
                    .build()
                client.newCall(request).execute().use { response ->
                    val elapsed = System.currentTimeMillis() - httpStart
                    elapsed.coerceAtLeast(15L)
                }
            } catch (ex: Exception) {
                -1L
            }
        }
    }
}
