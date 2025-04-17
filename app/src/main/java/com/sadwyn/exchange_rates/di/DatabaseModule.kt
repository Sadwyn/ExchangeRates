package com.sadwyn.exchange_rates.di

import android.content.Context
import androidx.room.Room
import com.sadwyn.exchange_rates.data.db.AppDatabase
import com.sadwyn.exchange_rates.data.db.dao.FavoriteAssetsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "exchange_rates_database"
        ).build()
    }

    @Provides
    fun provideAssetsDao(database: AppDatabase): FavoriteAssetsDao = database.favoriteAssetsDao()
}