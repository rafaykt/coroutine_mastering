package com.lukaslechner.coroutineusecasesonandroid.playground.structured_concurrency

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(Dispatchers.Default)

    scope.coroutineContext[Job]!!.invokeOnCompletion { throwable ->
        if(throwable is CancellationException){
            println("Parent was cancelled")
        }
    }
    val childCoroutine1Job = scope.launch {
        delay(1000)
        println("Coroutine 1 completed")
    }

    childCoroutine1Job.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Coroutine 1 was cancelled")
        }
    }
    scope.launch {
        delay(1000)
        println("Coroutine 2 completed")
    }.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Coroutine 2 was cancelled")
        }
    }
    delay(200)
    childCoroutine1Job.cancelAndJoin()
//    scope.cancel()
//    scope.coroutineContext[Job]!!.cancelAndJoin()
    /*
    * com a saída, é possivel entender que:
    * ->>> ao cancelar uma job de um filho, não se cancela nem o pai nem o irmão
    * */
}