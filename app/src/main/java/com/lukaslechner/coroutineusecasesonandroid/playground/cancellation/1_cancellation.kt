package com.lukaslechner.coroutineusecasesonandroid.playground.cancellation

import kotlinx.coroutines.*

fun main() = runBlocking {
    val job = launch {
        repeat(10) { index ->
            println("Operation number $index")

            try {
                delay(100)
            }catch(exception: CancellationException){
                println("Cancellation was thrown!!")
                throw CancellationException()
            }
        }
    }
    delay(250)
    println("Cancelling coroutine")
    job.cancel()
}