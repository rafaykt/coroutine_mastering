package com.lukaslechner.coroutineusecasesonandroid.playground.structured_concurrency

import kotlinx.coroutines.*

fun main() {

    val scopeJob = Job()
    val scope = CoroutineScope(Dispatchers.Default + scopeJob)

    var childCoroutineJob: Job? = null
    val coroutineJob = scope.launch {
        childCoroutineJob = launch {
            println("starting coroutine child")
            delay(100)
        }
        println("Starting coroutine")
        delay(1000)
    }

    Thread.sleep(100)

    println("Is childCoroutineJob a child of coroutineJob? ---> ${coroutineJob.children.contains(childCoroutineJob)}")

    println("is coroutinejob a child of scopejob? ---> ${scopeJob.children.contains(coroutineJob)}")

}

