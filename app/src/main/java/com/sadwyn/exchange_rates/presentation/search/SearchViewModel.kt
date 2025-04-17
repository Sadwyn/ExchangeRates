package com.sadwyn.exchange_rates.presentation.search

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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val ratesRepository: RatesRepository,
    private val assetsRepository: AssetsRepository
) : ViewModel() {

    private val _currenciesState = MutableStateFlow<List<CurrencyDataItem>>(emptyList())
    val currenciesState: StateFlow<List<CurrencyDataItem>> = _currenciesState

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _singleEventChannel = Channel<SearchScreenEvent>()
    val singleEventChannel: Flow<SearchScreenEvent> = _singleEventChannel.receiveAsFlow()

    fun onViewCreated(){
        loadCurrencies()
    }

    private fun loadCurrencies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                showLoading(true)
                val currencies = ratesRepository.loadAllCurrencies()
                val favorites = assetsRepository.getAllFavoriteAssets()
                val currencyDataList = currencies.map { entry ->
                    CurrencyDataItem(
                        entry.key,
                        entry.value,
                        favorites.find { entry.key == it.name } != null
                    )
                }
                _currenciesState.value = currencyDataList
            } catch (e: Throwable) {
                _singleEventChannel.trySend(SearchScreenEvent.Error(e.message ?: "Unknown error"))
            } finally {
                showLoading(false)
            }
        }
    }

    fun addFavoriteAsset(name: String, value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                showLoading(true)
                assetsRepository.addAsset(name, value)
                val newState = _currenciesState.value.toMutableList()
                val addedItem = newState.find { it.title == name }
                if (addedItem != null) {
                    val index = newState.indexOf(addedItem)
                    if (index != -1) {
                        newState[index] = addedItem.copy(isAdded = true)
                        _currenciesState.value = newState
                    }
                }
            } catch (e: Throwable) {
                _singleEventChannel.trySend(SearchScreenEvent.Error(e.message ?: "Unknown error"))
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(shouldShow: Boolean) {
        _loadingState.tryEmit(shouldShow)
    }
}