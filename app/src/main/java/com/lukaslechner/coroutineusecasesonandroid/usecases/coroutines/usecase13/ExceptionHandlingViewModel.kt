package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase13

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.*
import retrofit2.HttpException
import timber.log.Timber

class ExceptionHandlingViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun handleExceptionWithTryCatch() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                api.getAndroidVersionFeatures(27)
            } catch (exc: Exception) {
                if (exc is HttpException) {
                    if (exc.code() == 500) {
                        uiState.value = UiState.Error("Erro 500 $exc")
                    } else {
                        uiState.value = UiState.Error("Erro que não é o 500 $exc")
                    }
                }
            }
        }
    }

    fun handleWithCoroutineExceptionHandler() {
        uiState.value = UiState.Loading
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            uiState.value = UiState.Error("Erro em : $throwable")
        }

        viewModelScope.launch(exceptionHandler) {
            api.getAndroidVersionFeatures(27)
        }

    }

    fun showResultsEvenIfChildCoroutineFails() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            supervisorScope {
                val oreoFeaturesDeferred = async {
                    api.getAndroidVersionFeatures(27)
                }
                val pieFeaturesDeferred = async {
                    api.getAndroidVersionFeatures(28)
                }
                val qFeaturesDeferred = async {
                    api.getAndroidVersionFeatures(29)
                }

                val versionFeatures = listOf(
                    oreoFeaturesDeferred,
                    pieFeaturesDeferred,
                    qFeaturesDeferred
                ).mapNotNull {
                    try {
                        it.await()
                    } catch (exception: Exception) {
                        Timber.e("Error loading Feature data")
                        null
                    }
                }

                uiState.value = UiState.Success(versionFeatures)
            }
        }
    }
}