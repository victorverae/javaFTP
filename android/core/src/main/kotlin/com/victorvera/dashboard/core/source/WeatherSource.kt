package com.victorvera.dashboard.core.source

import com.victorvera.dashboard.core.model.WeatherInfo
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

@Serializable
data class OpenMeteoResponse(
    @SerialName("current_weather") val currentWeather: CurrentWeatherDto
)

@Serializable
data class CurrentWeatherDto(
    val temperature: Double,
    @SerialName("windspeed") val windSpeed: Double,
    @SerialName("weathercode") val weatherCode: Int,
    val time: String
)

/** Pure parsing logic, kept separate from the network call so it is trivial to unit test. */
fun parseWeatherResponse(rawJson: String): WeatherInfo {
    val response = json.decodeFromString(OpenMeteoResponse.serializer(), rawJson)
    return WeatherInfo(
        temperatureCelsius = response.currentWeather.temperature,
        windSpeedKmh = response.currentWeather.windSpeed,
        weatherCode = response.currentWeather.weatherCode,
        observedAt = response.currentWeather.time
    )
}

/** Open-Meteo requires no API key. Defaults to Bogotá; pass your own coordinates if needed. */
class WeatherSource(
    private val httpClient: HttpClient,
    private val latitude: Double = 4.7110,
    private val longitude: Double = -74.0721
) : DashboardSource<WeatherInfo> {

    override val id: String = "weather"
    override val displayName: String = "Clima"

    override suspend fun load(): Result<WeatherInfo> = runCatching {
        val rawJson = httpClient.get("https://api.open-meteo.com/v1/forecast") {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("current_weather", true)
        }.bodyAsText()
        parseWeatherResponse(rawJson)
    }
}
