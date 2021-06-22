package com.lukaslechner.coroutineusecasesonandroid.playground.structured_concurrency

import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

fun main() {
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Caught exception $throwable")
    }
//    val scope = CoroutineScope(Job() + exceptionHandler)
    val scope = CoroutineScope(SupervisorJob() + exceptionHandler)
    scope.launch {
        println("Coroutine 1 starts")
        delay(50)
        println("coroutine 1 fails")
        throw RuntimeException()
    }

    scope.launch {
        println("Coroutine 2 starts")
        delay(500)
        println("coroutine 2 completed")
    }.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Coroutine 2 got cancelled")
        }
    }
    Thread.sleep(1000)

    println("Scope got cancelled? -> ${!scope.isActive}")
}
/**
 * O supervisor job não é cancelado caso haja um exception, já o job é
 * */