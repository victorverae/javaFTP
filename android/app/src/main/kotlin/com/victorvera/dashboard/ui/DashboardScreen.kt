package com.victorvera.dashboard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.victorvera.dashboard.R
import com.victorvera.dashboard.core.DashboardCardResult
import com.victorvera.dashboard.core.model.AstronautsInSpace
import com.victorvera.dashboard.core.model.CryptoSnapshot
import com.victorvera.dashboard.core.model.Quote
import com.victorvera.dashboard.core.model.WeatherInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.dashboard_title)) },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = stringResource(R.string.action_refresh)
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (val current = state) {
            is DashboardUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is DashboardUiState.Loaded -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(current.cards, key = { it.sourceId }) { card ->
                        DashboardCard(card)
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardCard(card: DashboardCardResult<*>) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = card.displayName, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            val value = card.result.getOrNull()
            when (value) {
                is WeatherInfo -> WeatherContent(value)
                is CryptoSnapshot -> CryptoContent(value)
                is Quote -> QuoteContent(value)
                is AstronautsInSpace -> AstronautsContent(value)
                else -> Text(
                    text = stringResource(R.string.state_error),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun WeatherContent(info: WeatherInfo) {
    Text("${info.temperatureCelsius} °C · viento ${info.windSpeedKmh} km/h")
}

@Composable
private fun CryptoContent(snapshot: CryptoSnapshot) {
    Column {
        snapshot.prices.forEach { price ->
            Text("${price.coinId}: \$${price.usdPrice}")
        }
    }
}

@Composable
private fun QuoteContent(quote: Quote) {
    Text("\"${quote.content}\" — ${quote.author}")
}

@Composable
private fun AstronautsContent(info: AstronautsInSpace) {
    Text("${info.totalPeople} personas: ${info.names.joinToString(", ")}")
}
