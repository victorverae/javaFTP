package com.victorvera.dashboard.core.source

import com.victorvera.dashboard.core.model.AstronautsInSpace
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

@Serializable
data class AstrosResponse(
    val people: List<PersonDto>,
    val number: Int
)

@Serializable
data class PersonDto(
    val name: String,
    val craft: String
)

/** Pure parsing logic, kept separate from the network call so it is trivial to unit test. */
fun parseAstronautsResponse(rawJson: String): AstronautsInSpace {
    val response = json.decodeFromString(AstrosResponse.serializer(), rawJson)
    return AstronautsInSpace(
        totalPeople = response.number,
        names = response.people.map { it.name }
    )
}

/**
 * Open Notify's astros endpoint requires no API key. It is only served over
 * plain HTTP, so cleartext traffic for this single host is explicitly
 * allowed in the app's network security config.
 */
class AstronautsSource(
    private val httpClient: HttpClient
) : DashboardSource<AstronautsInSpace> {

    override val id: String = "astronauts"
    override val displayName: String = "Personas en el espacio"

    override suspend fun load(): Result<AstronautsInSpace> = runCatching {
        val rawJson = httpClient.get("http://api.open-notify.org/astros.json").bodyAsText()
        parseAstronautsResponse(rawJson)
    }
}
