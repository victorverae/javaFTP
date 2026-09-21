package com.victorvera.dashboard.core.source

import com.victorvera.dashboard.core.model.Quote
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val json = Json { ignoreUnknownKeys = true }

@Serializable
data class QuotableResponse(
    val content: String,
    val author: String
)

/** Pure parsing logic, kept separate from the network call so it is trivial to unit test. */
fun parseQuoteResponse(rawJson: String): Quote {
    val response = json.decodeFromString(QuotableResponse.serializer(), rawJson)
    return Quote(content = response.content, author = response.author)
}

/** quotable.io's random endpoint requires no API key. */
class QuoteSource(
    private val httpClient: HttpClient
) : DashboardSource<Quote> {

    override val id: String = "quote"
    override val displayName: String = "Frase del día"

    override suspend fun load(): Result<Quote> = runCatching {
        val rawJson = httpClient.get("https://api.quotable.io/random").bodyAsText()
        parseQuoteResponse(rawJson)
    }
}
