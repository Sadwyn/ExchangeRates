package com.sadwyn.exchange_rates.data.repository

import com.sadwyn.exchange_rates.data.api.OpenExchangeApi
import com.sadwyn.exchange_rates.data.db.dao.FavoriteAssetsDao
import com.sadwyn.exchange_rates.data.db.entity.FavoriteAsset
import com.sadwyn.exchange_rates.domain.RatesRepository
import com.sadwyn.exchange_rates.domain.exceptions.NetworkException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject


class RatesRepositoryImpl @Inject constructor(
    private val openExchangeApi: OpenExchangeApi,
    private val favoriteAssetsDao: FavoriteAssetsDao,
) : RatesRepository {


    override suspend fun loadLatestRates() {
        val ratesResponse = openExchangeApi.getLatestExchangeRates()

        val ratesDataList = ratesResponse.rates?.let { safeRates ->
            safeRates.map {
                FavoriteAsset(
                    it.key,
                    it.value.toString(),
                    (it.value + randomDouble()).roundTo(5, Locale.getDefault())
                )
            }
        }.orEmpty()
        favoriteAssetsDao.updateOnlyExisting(ratesDataList)
    }

    private fun randomDouble(): Double {
        return (1..20).random() + Math.random()
    }

    override suspend fun loadAllCurrencies(): Map<String, String> {
        return try {
            openExchangeApi.getAllCurrencies()
        } catch (e: Throwable) {
            throw NetworkException("Problem with network", e)
        }
    }

    override suspend fun observeLatestRates(intervalMillis: Long): Flow<Throwable?> = flow {
        while (true) {
            try {
                loadLatestRates()
                emit(null)
            } catch (e: Throwable) {
                emit(NetworkException("Problem with network", e))
            }
            delay(intervalMillis)
        }
    }.flowOn(Dispatchers.IO)


    private fun Double.roundTo(decimals: Int, locale: Locale): Double {
        val formatted = String.format(locale, "%.${decimals}f", this)
        val format = NumberFormat.getInstance(locale)
        val number = format.parse(formatted)?.toDouble()
        return number ?: 0.0
    }
}