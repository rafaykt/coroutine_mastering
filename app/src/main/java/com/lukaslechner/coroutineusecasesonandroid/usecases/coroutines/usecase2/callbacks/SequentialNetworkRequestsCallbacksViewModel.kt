package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.callbacks

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : BaseViewModel<UiState>() {

    private var getAndroidVersionsCall: Call<List<AndroidVersion>>? = null
    private var getAndroidFeaturesCall: Call<VersionFeatures>? = null

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading
        val getAndroidVersionCall = mockApi.getRecentAndroidVersions()
        getAndroidVersionCall.enqueue(object : Callback<List<AndroidVersion>> {

            override fun onFailure(call: Call<List<AndroidVersion>>, t: Throwable) {
                uiState.value = UiState.Error("Something unexpected happened")
            }

            override fun onResponse(
                call: Call<List<AndroidVersion>>,
                response: Response<List<AndroidVersion>>
            ) {
                if (response.isSuccessful) {
                    val mostRecentVersion = response.body()?.let {
                        it.last()
                    }
                    val getAndroidFeatureCall =
                        mostRecentVersion?.let { mockApi.getAndroidVersionFeatures(it.apiLevel) }
                    getAndroidFeatureCall?.enqueue(object : Callback<VersionFeatures> {
                        override fun onFailure(call: Call<VersionFeatures>, t: Throwable) {
                            uiState.value = UiState.Error("Something wrong here. ?")
                        }

                        override fun onResponse(
                            call: Call<VersionFeatures>,
                            response: Response<VersionFeatures>
                        ) {
                            if (response.isSuccessful) {
                                val featuresOfMostRecentVersion = response.body()
                                uiState.value = UiState.Success(featuresOfMostRecentVersion!!)
                            } else {
                                uiState.value = UiState.Error("Network error")
                            }
                        }

                    })

                } else {
                    uiState.value = UiState.Error("Network Request Failed")
                }
            }
        })


    }
    override fun onCleared() {
        super.onCleared()
        getAndroidVersionsCall?.cancel()
        getAndroidFeaturesCall?.cancel()

    }
}