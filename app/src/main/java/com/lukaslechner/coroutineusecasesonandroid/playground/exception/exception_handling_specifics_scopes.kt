package com.lukaslechner.coroutineusecasesonandroid.playground.exception

import kotlinx.coroutines.*
import java.lang.RuntimeException

fun main () = runBlocking{
    /**
     * o coroutinescope dentro do runblocking consegue então manipular o erro
     * */
    try {
        doSomethingSuspend()
    }catch(e: Exception){
        println("Caught $e")
    }



}

private suspend fun doSomethingSuspend(){
    coroutineScope{
        launch{
            throw RuntimeException()
        }
    }
}