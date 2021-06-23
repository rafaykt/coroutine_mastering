package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase11

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import timber.log.Timber
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class CooperativeCancellationViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    var calculationJob: Job? = null
    fun performCalculation(factorialOf: Int) {
        uiState.value = UiState.Loading
        calculationJob = viewModelScope.launch {
            var result = BigInteger.ONE
            val computationDuration = measureTimeMillis {
                result = calculateFactorial(factorialOf)
            }
            var resultToString = ""
            val toStringDuration = measureTimeMillis {
                resultToString = withContext(Dispatchers.IO) { result.toString() }
            }

            uiState.value = UiState.Success(resultToString,computationDuration, toStringDuration)
        }
        calculationJob?.invokeOnCompletion { throwable ->
            if(throwable is CancellationException){
              Timber.d ("Calculation was cancelled!!")
            }
        }
    }

    private suspend fun calculateFactorial(number: Int) = withContext(Dispatchers.IO) {
        var factorialResult = BigInteger.ONE
        for (iteradorFatorial in 1..number) {
            yield()
            factorialResult =
                factorialResult.multiply(BigInteger.valueOf(iteradorFatorial.toLong()))
        }
        factorialResult
    }

    fun cancelCalculation() {
        calculationJob?.cancel()
    }

    fun uiState(): LiveData<UiState> = uiState

    private val uiState: MutableLiveData<UiState> = MutableLiveData()
}