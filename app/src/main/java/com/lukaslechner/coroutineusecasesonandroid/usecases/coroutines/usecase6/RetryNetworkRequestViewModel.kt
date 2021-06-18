package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.delay
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

    private suspend fun <T> retry(
        numberOfRetries: Int,
        initialDelayMilis: Long = 100,
        maxDelayMilis: Long = 1000,
        factor: Double = 2.0,
        block: suspend () -> T):T {
        var currentDelay = initialDelayMilis


        repeat(numberOfRetries) {
            try {
                return  block()
            } catch (exception: Exception) {
                Timber.e(exception)
            }
        }
        delay(currentDelay)
        currentDelay = ((currentDelay * factor).toLong()).coerceAtMost(maxDelayMilis)

        return block()
    }

    private suspend fun loadRecentAndroidVersion() {
        val recentAndroidVersions = api.getRecentAndroidVersions()
        uiState.value = UiState.Success(recentAndroidVersions)
    }

}