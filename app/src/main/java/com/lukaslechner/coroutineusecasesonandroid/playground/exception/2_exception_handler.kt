package com.lukaslechner.coroutineusecasesonandroid.playground.exception

import kotlinx.coroutines.*
import java.lang.RuntimeException

fun main() {
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Caught $throwable in Coroutine Exception handler")
    }

    val scope = CoroutineScope(Job() + exceptionHandler)

    scope.launch(){
        launch(){
            throw RuntimeException()
        }
    }

    Thread.sleep(100)
}