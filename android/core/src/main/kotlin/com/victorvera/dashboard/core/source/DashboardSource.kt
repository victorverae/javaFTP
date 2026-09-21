package com.victorvera.dashboard.core.source

/** A single data source that feeds one card of the dashboard. */
interface DashboardSource<T> {
    val id: String
    val displayName: String
    suspend fun load(): Result<T>
}
