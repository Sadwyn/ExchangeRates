package com.sadwyn.exchange_rates.data.network

interface TokenProvider {
    fun getToken(): String
}