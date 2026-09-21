package com.victorvera.dashboard.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout

/**
 * CIO is a pure Kotlin/JVM engine, so this same client works both in JVM
 * unit tests and on-device (no platform-specific engine needed).
 */
fun createDashboardHttpClient(): HttpClient = HttpClient(CIO) {
    install(HttpTimeout) {
        requestTimeoutMillis = 10_000
        connectTimeoutMillis = 10_000
    }
}
