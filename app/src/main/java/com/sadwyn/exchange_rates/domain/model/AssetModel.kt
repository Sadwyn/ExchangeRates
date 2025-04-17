package com.sadwyn.exchange_rates.domain.model

data class AssetModel(
    val name: String,
    val value: String,
    val cost: String?
)