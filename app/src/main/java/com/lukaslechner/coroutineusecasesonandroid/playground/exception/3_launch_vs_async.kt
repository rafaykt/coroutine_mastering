package com.lukaslechner.coroutineusecasesonandroid.playground.exception

import kotlinx.coroutines.*
import java.lang.RuntimeException

fun main (){
    val exceptionHandler = CoroutineExceptionHandler{coroutineContext, throwable->
        println("Caught ${throwable} in coroutineExceptionhandler")
    }
    val scope = CoroutineScope(Job() + exceptionHandler)

    /**
     * Ver sobre async exception handler na teoria.
     * */
    scope.async {
        async{
            delay(200)
            throw RuntimeException()
        }

    }

    Thread.sleep(500)
}