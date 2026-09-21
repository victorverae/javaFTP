package com.victorvera.dashboard.core

import com.victorvera.dashboard.core.source.DashboardSource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

data class DashboardCardResult<T>(
    val sourceId: String,
    val displayName: String,
    val result: Result<T>
)

/**
 * Loads every configured source concurrently. A failure in one source
 * (network error, bad payload, etc.) never prevents the others from
 * loading: each result is captured independently.
 */
class DashboardAggregator(private val sources: List<DashboardSource<*>>) {
    suspend fun loadAll(): List<DashboardCardResult<*>> = coroutineScope {
        sources
            .map { source -> async { DashboardCardResult(source.id, source.displayName, source.load()) } }
            .map { it.await() }
    }
}
