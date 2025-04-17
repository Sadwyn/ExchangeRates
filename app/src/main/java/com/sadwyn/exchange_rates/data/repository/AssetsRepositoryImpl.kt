package com.sadwyn.exchange_rates.data.repository

import com.sadwyn.exchange_rates.data.db.dao.FavoriteAssetsDao
import com.sadwyn.exchange_rates.data.db.entity.FavoriteAsset
import com.sadwyn.exchange_rates.domain.AssetsRepository
import com.sadwyn.exchange_rates.domain.model.AssetModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AssetsRepositoryImpl @Inject constructor(
    private val favoriteAssetsDao: FavoriteAssetsDao,
) : AssetsRepository {

    override suspend fun subscribeAllFavoriteAssets(): Flow<List<AssetModel>> {
        return favoriteAssetsDao.subscribeAllFavoriteAssets()
            .map { asset -> asset.map { AssetModel(it.name, it.value, it.rate?.toString()) } }
    }

    override suspend fun getAllFavoriteAssets(): List<AssetModel> {
        return favoriteAssetsDao.getAllFavoriteAssets()
            .map { AssetModel(it.name, it.value, it.rate?.toString()) }
    }

    override suspend fun addAsset(name: String, value: String) {
        favoriteAssetsDao.insert(FavoriteAsset(name = name, value = value, rate = null))
    }

    override suspend fun removeAsset(name: String) {
        favoriteAssetsDao.deleteAssetByName(name)
    }
}