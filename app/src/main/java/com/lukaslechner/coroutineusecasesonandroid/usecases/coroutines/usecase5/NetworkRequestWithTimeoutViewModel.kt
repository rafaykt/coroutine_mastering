package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

class NetworkRequestWithTimeoutViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest(timeout: Long) {
        uiState.value=UiState.Loading

        usingWithTimeoutOrNull(timeout)
    }

    private fun usingWithTimeout(timeout: Long) {
        viewModelScope.launch {
            try {
                val recentAndroidVersioins = withTimeout(timeout) {
                    api.getRecentAndroidVersions()
                }
                uiState.value = UiState.Success(recentAndroidVersioins)
            } catch (timeoutCancellationException: TimeoutCancellationException) {
                uiState.value = UiState.Error("Network request timed out!")
            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network request failed")
            }

        }
    }

    private fun usingWithTimeoutOrNull(timeout: Long) {
        viewModelScope.launch {
            try {
                val recentAndroidVersioins = withTimeoutOrNull(timeout) {
                    api.getRecentAndroidVersions()
                }
                if(recentAndroidVersioins!= null) {
                    uiState.value = UiState.Success(recentAndroidVersioins)
                }else{
                    uiState.value= UiState.Error("Network request time out")
                }
            }catch (exception: Exception) {
                uiState.value = UiState.Error("Network request failed")
            }

        }
    }

}