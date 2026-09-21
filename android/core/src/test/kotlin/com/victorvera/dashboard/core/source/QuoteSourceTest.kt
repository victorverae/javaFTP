package com.victorvera.dashboard.core.source

import kotlin.test.Test
import kotlin.test.assertEquals

class QuoteSourceTest {

    @Test
    fun `parses quotable response ignoring unknown fields`() {
        val rawJson = """
            {
              "_id": "abc123",
              "content": "Simplicity is the ultimate sophistication.",
              "author": "Leonardo da Vinci",
              "tags": ["wisdom"],
              "length": 42
            }
        """.trimIndent()

        val quote = parseQuoteResponse(rawJson)

        assertEquals("Simplicity is the ultimate sophistication.", quote.content)
        assertEquals("Leonardo da Vinci", quote.author)
    }
}
