package com.sadwyn.exchange_rates.data

import com.google.gson.annotations.SerializedName

data class RateResponse(
    @SerializedName("base")
    val base: String?,
    @SerializedName("disclaimer")
    val disclaimer: String?,
    @SerializedName("license")
    val license: String?,
    @SerializedName("rates")
    val rates: Map<String, Double>?,
    @SerializedName("timestamp")
    val timestamp: Int?
)


