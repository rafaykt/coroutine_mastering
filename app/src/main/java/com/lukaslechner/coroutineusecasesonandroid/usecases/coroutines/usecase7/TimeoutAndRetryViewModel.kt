package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import kotlinx.coroutines.*
import timber.log.Timber

class TimeoutAndRetryViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {
    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        val numberOfRetries = 2
        val timeOut = 1000L
        val oreoVersionDeferred = viewModelScope.async{
            retryWithTimeout(numberOfRetries, timeOut){
                api.getAndroidVersionFeatures(27)
            }
        }
        val pieVersionDeferred = viewModelScope.async{
            retryWithTimeout(numberOfRetries, timeOut){
                api.getAndroidVersionFeatures(28)
            }
        }

        viewModelScope.launch{
            try{
                val versionFeatures = listOf(oreoVersionDeferred, pieVersionDeferred ).awaitAll()
                uiState.value=UiState.Success(versionFeatures)
            }catch(e: Exception){
                Timber.e("Network Request Failed")
                uiState.value= UiState.Error("Buuuuuuuuug")
            }

        }

    }

//        run api.getAndroidVersionFeatures(27) and api.getAndroidVersionFeatures(28) in parallel

    private suspend fun <T> retry(
        numberOfRetries: Int,
        delayBetweenRetries: Long = 100,
        block: suspend () -> T
    ): T {
        repeat(numberOfRetries) {
            try {
                return block()
            } catch (exception: Exception) {
                Timber.e(exception)
            }
        }
        delay(delayBetweenRetries)
        return block()
    }


    private suspend fun <T> retryWithTimeout(
        numberOfRetries: Int,
        timeOut: Long,
        block: suspend () -> T
    ) = retry(numberOfRetries) {
        withTimeout(timeOut){
            block()
        }
    }


    private suspend fun loadRecentAndroidVersion(version: Int): VersionFeatures {
        return api.getAndroidVersionFeatures(version)
    }
}