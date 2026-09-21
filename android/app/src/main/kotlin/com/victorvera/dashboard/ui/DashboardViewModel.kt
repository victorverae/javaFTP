package com.victorvera.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.victorvera.dashboard.core.DashboardAggregator
import com.victorvera.dashboard.core.DashboardCardResult
import com.victorvera.dashboard.core.network.createDashboardHttpClient
import com.victorvera.dashboard.core.source.AstronautsSource
import com.victorvera.dashboard.core.source.CryptoSource
import com.victorvera.dashboard.core.source.QuoteSource
import com.victorvera.dashboard.core.source.WeatherSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Loaded(val cards: List<DashboardCardResult<*>>) : DashboardUiState
}

class DashboardViewModel(
    private val aggregator: DashboardAggregator
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            val cards = aggregator.loadAll()
            _uiState.value = DashboardUiState.Loaded(cards)
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val httpClient = createDashboardHttpClient()
                val aggregator = DashboardAggregator(
                    listOf(
                        WeatherSource(httpClient),
                        CryptoSource(httpClient),
                        QuoteSource(httpClient),
                        AstronautsSource(httpClient)
                    )
                )
                return DashboardViewModel(aggregator) as T
            }
        }
    }
}
