package com.sadwyn.exchange_rates.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sadwyn.exchange_rates.data.db.entity.FavoriteAsset
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAssetsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: FavoriteAsset)

    @Query("SELECT * FROM favorite_assets")
    fun subscribeAllFavoriteAssets(): Flow<List<FavoriteAsset>>

    @Query("SELECT * FROM favorite_assets")
    fun getAllFavoriteAssets(): List<FavoriteAsset>

    @Query("""UPDATE favorite_assets SET rate = :rate WHERE name = :name""")
    fun updateAssetByName(name: String, rate: Double?)

    @Query("""DELETE FROM favorite_assets WHERE name = :name""")
    fun deleteAssetByName(name: String)

    @Transaction
    fun updateOnlyExisting(list: List<FavoriteAsset>) {
        list.forEach {
            updateAssetByName(it.name, it.rate)
        }
    }

    @Delete
    fun deleteAsset(user: FavoriteAsset)
}