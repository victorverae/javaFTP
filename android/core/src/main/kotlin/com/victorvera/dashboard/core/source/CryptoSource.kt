package com.victorvera.dashboard.core.source

import com.victorvera.dashboard.core.model.CryptoPrice
import com.victorvera.dashboard.core.model.CryptoSnapshot
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private val json = Json { ignoreUnknownKeys = true }

/** Pure parsing logic, kept separate from the network call so it is trivial to unit test. */
fun parseCryptoResponse(rawJson: String): CryptoSnapshot {
    val root = json.parseToJsonElement(rawJson).jsonObject
    val prices = root.entries.map { (coinId, values) ->
        val usd = values.jsonObject.getValue("usd").jsonPrimitive.double
        CryptoPrice(coinId = coinId, usdPrice = usd)
    }.sortedBy { it.coinId }
    return CryptoSnapshot(prices)
}

private fun JsonObject.getValue(key: String) =
    this[key] ?: error("Missing key '$key' in crypto response")

/** CoinGecko's simple price endpoint requires no API key. */
class CryptoSource(
    private val httpClient: HttpClient,
    private val coinIds: List<String> = listOf("bitcoin", "ethereum", "solana")
) : DashboardSource<CryptoSnapshot> {

    override val id: String = "crypto"
    override val displayName: String = "Criptomonedas"

    override suspend fun load(): Result<CryptoSnapshot> = runCatching {
        val rawJson = httpClient.get("https://api.coingecko.com/api/v3/simple/price") {
            parameter("ids", coinIds.joinToString(","))
            parameter("vs_currencies", "usd")
        }.bodyAsText()
        parseCryptoResponse(rawJson)
    }
}
