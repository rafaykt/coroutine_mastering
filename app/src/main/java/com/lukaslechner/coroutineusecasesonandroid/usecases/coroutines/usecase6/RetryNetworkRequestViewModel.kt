package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch
import timber.log.Timber

class RetryNetworkRequestViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            val numberOfTries = 2

            try {
                retry(numberOfTries ){
                    loadRecentAndroidVersion()
                }
            } catch (exception: Exception) {
                Timber.e(exception)
            }
        }
    }

    private suspend fun <T> retry(numberOfRetries: Int, block: suspend () -> T):T {
        repeat(numberOfRetries) {
            try {
                return  block()
            } catch (exception: Exception) {
                Timber.e(exception)
            }
        }
        return block()
    }

    private suspend fun loadRecentAndroidVersion() {
        val recentAndroidVersions = api.getRecentAndroidVersions()
        uiState.value = UiState.Success(recentAndroidVersions)
    }

}