package com.sadwyn.exchange_rates.presentation.dashboard

sealed interface DashboardScreenState {
    class Error(val message: String) : DashboardScreenState
}