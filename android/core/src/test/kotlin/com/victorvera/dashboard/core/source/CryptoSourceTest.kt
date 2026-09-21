package com.victorvera.dashboard.core.source

import com.victorvera.dashboard.core.model.CryptoPrice
import kotlin.test.Test
import kotlin.test.assertEquals

class CryptoSourceTest {

    @Test
    fun `parses coingecko response into sorted CryptoSnapshot`() {
        val rawJson = """
            {
              "ethereum": { "usd": 3400.5 },
              "bitcoin": { "usd": 63000.12 }
            }
        """.trimIndent()

        val snapshot = parseCryptoResponse(rawJson)

        assertEquals(
            listOf(
                CryptoPrice("bitcoin", 63000.12),
                CryptoPrice("ethereum", 3400.5)
            ),
            snapshot.prices
        )
    }
}
