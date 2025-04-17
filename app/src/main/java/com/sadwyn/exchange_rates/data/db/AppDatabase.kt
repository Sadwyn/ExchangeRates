package com.sadwyn.exchange_rates.data.db

import androidx.room.RoomDatabase
import com.sadwyn.exchange_rates.data.db.dao.FavoriteAssetsDao
import com.sadwyn.exchange_rates.data.db.entity.FavoriteAsset

@androidx.room.Database(
    entities = [
        FavoriteAsset::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteAssetsDao(): FavoriteAssetsDao
}