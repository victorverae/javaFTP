package com.victorvera.dashboard.core.source

import kotlin.test.Test
import kotlin.test.assertEquals

class WeatherSourceTest {

    @Test
    fun `parses open-meteo response into WeatherInfo`() {
        val rawJson = """
            {
              "latitude": 4.71,
              "longitude": -74.07,
              "current_weather": {
                "temperature": 18.5,
                "windspeed": 12.3,
                "weathercode": 3,
                "time": "2026-09-21T15:00"
              }
            }
        """.trimIndent()

        val info = parseWeatherResponse(rawJson)

        assertEquals(18.5, info.temperatureCelsius)
        assertEquals(12.3, info.windSpeedKmh)
        assertEquals(3, info.weatherCode)
        assertEquals("2026-09-21T15:00", info.observedAt)
    }
}
