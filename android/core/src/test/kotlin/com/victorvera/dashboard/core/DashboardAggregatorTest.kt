package com.victorvera.dashboard.core

import com.victorvera.dashboard.core.source.DashboardSource
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private class FakeSource(
    override val id: String,
    override val displayName: String,
    private val block: suspend () -> Result<String>
) : DashboardSource<String> {
    override suspend fun load(): Result<String> = block()
}

class DashboardAggregatorTest {

    @Test
    fun `loads every source and keeps failures isolated`() = runTest {
        val ok = FakeSource("ok", "OK") { Result.success("valor") }
        val failing = FakeSource("fail", "Fail") { Result.failure(IllegalStateException("boom")) }

        val results = DashboardAggregator(listOf(ok, failing)).loadAll()

        assertEquals(2, results.size)
        assertEquals("valor", results.first { it.sourceId == "ok" }.result.getOrNull())
        assertTrue(results.first { it.sourceId == "fail" }.result.isFailure)
    }
}
