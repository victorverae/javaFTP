package com.victorvera.dashboard.core.source

import kotlin.test.Test
import kotlin.test.assertEquals

class AstronautsSourceTest {

    @Test
    fun `parses open-notify response into AstronautsInSpace`() {
        val rawJson = """
            {
              "people": [
                { "name": "Jasmin Moghbeli", "craft": "ISS" },
                { "name": "Oleg Kononenko", "craft": "ISS" }
              ],
              "number": 2,
              "message": "success"
            }
        """.trimIndent()

        val info = parseAstronautsResponse(rawJson)

        assertEquals(2, info.totalPeople)
        assertEquals(listOf("Jasmin Moghbeli", "Oleg Kononenko"), info.names)
    }
}
