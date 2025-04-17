package com.sadwyn.exchange_rates.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadwyn.exchange_rates.domain.AssetsRepository
import com.sadwyn.exchange_rates.domain.RatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val assetsRepository: AssetsRepository,
    private val ratesRepository: RatesRepository
) : ViewModel() {

    private val _currenciesState = MutableStateFlow<List<DashboardDataItem>>(emptyList())
    val currenciesState: StateFlow<List<DashboardDataItem>> = _currenciesState

    private val _singleEventChannel = Channel<DashboardScreenState>()
    val singleEventChannel: Flow<DashboardScreenState> = _singleEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            assetsRepository.subscribeAllFavoriteAssets()
                .flowOn(Dispatchers.IO)
                .collectLatest { favorites ->
                    withContext(Dispatchers.Main.immediate) {
                        _currenciesState.value = favorites.map {
                            DashboardDataItem(
                                title = it.name,
                                value = it.value,
                                cost = it.cost,
                            )
                        }
                    }
                }
        }
        viewModelScope.launch {
            ratesRepository.observeLatestRates().collect { error ->
                if (error != null) {
                    _singleEventChannel.trySend(
                        DashboardScreenState.Error(
                            error.message ?: "Unkown Error"
                        )
                    )
                }
            }
        }
    }

    fun removeFavoriteAsset(asset: DashboardDataItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                assetsRepository.removeAsset(asset.title)
            } catch (e: Throwable) {
                _singleEventChannel.trySend(
                    DashboardScreenState.Error(
                        e.message ?: "Unknown error"
                    )
                )
            }
        }
    }
}