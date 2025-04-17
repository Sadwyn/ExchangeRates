package com.sadwyn.exchange_rates.domain

import kotlinx.coroutines.flow.Flow

interface RatesRepository {
    suspend fun observeLatestRates(intervalMillis: Long = 5000L): Flow<Throwable?>
    suspend fun loadLatestRates()
    suspend fun loadAllCurrencies(): Map<String, String>
}