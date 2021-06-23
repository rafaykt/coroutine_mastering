package com.lukaslechner.coroutineusecasesonandroid.playground.cancellation

import kotlinx.coroutines.*

fun main() = runBlocking {

    val job = launch(Dispatchers.Default) {
        repeat(10) { index ->
            /*yield()
            println("Operation number $index")
            Thread.sleep(100)*/
            if (isActive) {
                println("Operation number $index")
                Thread.sleep(100)
            } else {
                withContext(NonCancellable) {
                    delay(100)
                    println("Cancelling...")
                    throw CancellationException()
                }
            }
        }
    }
    delay(250)
    println("Cancelling Coroutine")
    job.cancel()
}