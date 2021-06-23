package com.lukaslechner.coroutineusecasesonandroid.playground.exception

import kotlinx.coroutines.*

fun main() {
    val scope = CoroutineScope(Job())
    scope.launch {
        try {
            /**
             * ao descomentar as linhas do launch, o código não vai pegar o disparo do erro, e vai crashar*/
//            launch {
                functionThatThrows()
//            }
        } catch (e: Exception) {
            println("pegou essa exception: ${e}")
        }
    }
    Thread.sleep(100)

}

private fun functionThatThrows() {
    //somecode
    throw RuntimeException()
}