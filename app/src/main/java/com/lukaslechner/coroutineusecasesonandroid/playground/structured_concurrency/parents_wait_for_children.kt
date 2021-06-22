package com.lukaslechner.coroutineusecasesonandroid.playground.structured_concurrency

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(Dispatchers.Default)
    val parentCoroutineJob = scope.launch {
        launch {
            delay(1030)
            println("Child coroutine 1 completed")
        }
        launch {
            delay(1000)
            println("child coroutine 2 fcompleted")
        }

    }
    parentCoroutineJob.join()
    println("Parent job completed")

}