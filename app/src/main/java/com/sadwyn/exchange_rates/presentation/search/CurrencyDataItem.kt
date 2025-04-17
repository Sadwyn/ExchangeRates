package com.sadwyn.exchange_rates.presentation.search

data class CurrencyDataItem(
    val title: String,
    val value: String,
    var isAdded: Boolean = false
)