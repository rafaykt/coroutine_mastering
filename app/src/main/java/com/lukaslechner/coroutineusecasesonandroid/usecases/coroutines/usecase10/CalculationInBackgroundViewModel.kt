package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class CalculationInBackgroundViewModel : BaseViewModel<UiState>() {

    fun performCalculation(factorialOf: Int) {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            var result = BigInteger.ONE
            val computationDuration = measureTimeMillis {
                result = calculateFactorial(factorialOf)
            }
            var resultToString = ""
            val toStringDuration = measureTimeMillis {
                resultToString = withContext(Dispatchers.IO) { result.toString() }
            }

            uiState.value = UiState.Success(resultToString, computationDuration, toStringDuration)
        }
    }

    private suspend fun calculateFactorial(number: Int) = withContext(Dispatchers.IO) {
        var factorialResult = BigInteger.ONE
        for (iteradorFatorial in 1..number) {
            factorialResult =
                factorialResult.multiply(BigInteger.valueOf(iteradorFatorial.toLong()))
        }
        factorialResult
    }
}