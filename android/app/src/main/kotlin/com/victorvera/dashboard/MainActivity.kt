package com.victorvera.dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.victorvera.dashboard.ui.DashboardScreen
import com.victorvera.dashboard.ui.DashboardViewModel
import com.victorvera.dashboard.ui.theme.DashboardTheme

class MainActivity : ComponentActivity() {

    private val viewModel: DashboardViewModel by viewModels { DashboardViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DashboardTheme {
                DashboardScreen(viewModel = viewModel)
            }
        }
    }
}
