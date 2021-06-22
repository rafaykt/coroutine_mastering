package com.lukaslechner.coroutineusecasesonandroid.playground.structured_concurrency

import kotlinx.coroutines.*

fun main(){
    println("Job of globalscope: ${GlobalScope.coroutineContext[Job]}")

    GlobalScope.launch{}
}