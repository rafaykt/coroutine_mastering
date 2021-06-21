package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.lang.Exception

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value=UiState.Loading
        viewModelScope.launch{
            try {
                val api27 = mockApi.getAndroidVersionFeatures(27)
                val api28 = mockApi.getAndroidVersionFeatures(28)
                val api29 = mockApi.getAndroidVersionFeatures(29)
                val arrayApi = listOf(api27, api28, api29)

                uiState.value = UiState.Success(arrayApi)
            }catch(e:Exception){
                UiState.Error("Network internal error")
            }


        }
    }

    fun performNetworkRequestsConcurrently() {
        uiState.value=UiState.Loading
            try {
                val api27 = viewModelScope.async{ mockApi.getAndroidVersionFeatures(27)}
                val api28 = viewModelScope.async{mockApi.getAndroidVersionFeatures(28)}
                val api29 = viewModelScope.async{mockApi.getAndroidVersionFeatures(29)}
                viewModelScope.launch{
                    val arrayApi = awaitAll(api27, api28, api29)
                    uiState.value = UiState.Success(arrayApi)
                }

            }catch(e:Exception){
                UiState.Error("Network internal error")
            }
    }
}