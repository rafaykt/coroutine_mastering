package com.lukaslechner.coroutineusecasesonandroid.playground.exception

import kotlinx.coroutines.*

fun main()  {

    val exceptionHandler = CoroutineExceptionHandler{coroutineContext, throwable ->
        println("Caught $throwable in CoroutineExceptionHandler")
    }

    val scope = CoroutineScope (Job() + exceptionHandler)

    scope.launch{
        try{
            supervisorScope {
                launch{
                    println("Exception:${ coroutineContext[CoroutineExceptionHandler]}")
                    throw RuntimeException()
                }
            }
        }catch(e: Exception){
            println("Caught $e")
        }
    }

    /**
     * O supervisor coroutine herda o contexto inteiro do pai, mas sobrescreve o job por um supervisorjob
     * Usa-se o supervisorScope quando: Você quer manter o Pai sem quebrar caso os filhos disparem um erro
     * Usa-se o scope coroutine scope quando quer que o pai quebre quando os filhos dispararem um erro
     * */

    Thread.sleep(600)
}