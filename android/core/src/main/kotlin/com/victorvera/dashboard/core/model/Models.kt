package com.victorvera.dashboard.core.model

data class WeatherInfo(
    val temperatureCelsius: Double,
    val windSpeedKmh: Double,
    val weatherCode: Int,
    val observedAt: String
)

data class CryptoPrice(
    val coinId: String,
    val usdPrice: Double
)

data class CryptoSnapshot(
    val prices: List<CryptoPrice>
)

data class Quote(
    val content: String,
    val author: String
)

data class AstronautsInSpace(
    val totalPeople: Int,
    val names: List<String>
)
