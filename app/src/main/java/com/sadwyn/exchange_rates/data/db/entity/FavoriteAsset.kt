package com.sadwyn.exchange_rates.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_assets",
    indices = [Index(value = ["name"], unique = true)]
)
data class FavoriteAsset(
    @PrimaryKey
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("value")
    val value: String,
    @ColumnInfo("rate")
    val rate: Double?
)