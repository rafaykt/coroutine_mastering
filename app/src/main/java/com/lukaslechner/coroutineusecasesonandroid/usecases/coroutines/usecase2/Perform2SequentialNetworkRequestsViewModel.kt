package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {
    fun perform2SequentialNetworkRequest() {
    uiState.value = UiState.Loading
        viewModelScope.launch{
            try{
                val recentVersions = mockApi.getRecentAndroidVersions()
                val mostRecentVersions = recentVersions.last()
                val featuresOfMostRecentVersions = mockApi.getAndroidVersionFeatures((mostRecentVersions.apiLevel))
                uiState.value = UiState.Success(featuresOfMostRecentVersions)
                println(Thread.currentThread().name)
            }catch(exception: Exception){
                uiState.value = UiState.Error("Network request Failed")
            }
        }

    }
}