package com.sadwyn.exchange_rates.presentation.search

sealed interface SearchScreenEvent {
    class Error(val message: String) : SearchScreenEvent
}