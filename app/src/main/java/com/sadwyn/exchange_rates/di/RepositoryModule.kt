package com.sadwyn.exchange_rates.di

import com.sadwyn.exchange_rates.data.repository.AssetsRepositoryImpl
import com.sadwyn.exchange_rates.data.repository.RatesRepositoryImpl
import com.sadwyn.exchange_rates.domain.AssetsRepository
import com.sadwyn.exchange_rates.domain.RatesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAssetsRepository(
        impl: AssetsRepositoryImpl
    ): AssetsRepository

    @Binds
    @Singleton
    abstract fun bindRatesRepository(
        impl: RatesRepositoryImpl
    ): RatesRepository
}