package com.sadwyn.exchange_rates.data.api

import com.sadwyn.exchange_rates.data.RateResponse
import retrofit2.http.GET

interface OpenExchangeApi {
    @GET("api/latest.json")
    suspend fun getLatestExchangeRates(): RateResponse

    @GET("api/currencies.json")
    suspend fun getAllCurrencies(): Map<String, String>
}