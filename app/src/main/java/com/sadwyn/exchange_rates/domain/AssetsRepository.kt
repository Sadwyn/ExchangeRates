package com.sadwyn.exchange_rates.domain

import com.sadwyn.exchange_rates.domain.model.AssetModel
import kotlinx.coroutines.flow.Flow

interface AssetsRepository {
    suspend fun subscribeAllFavoriteAssets(): Flow<List<AssetModel>>
    suspend fun getAllFavoriteAssets(): List<AssetModel>
    suspend fun addAsset(name: String, value: String)
    suspend fun removeAsset(name: String)
}