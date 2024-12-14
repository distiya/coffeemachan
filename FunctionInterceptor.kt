package com.github.distiya.first_service

fun <R> withTracing(
        name: String,
        contextualName: String
): (suspend ()->R?) -> (suspend ()->R?){
    return { block ->
        {
            println("Intercepting with tracing $name and $contextualName")
            block()
        }
    }
}

fun <R> withAuthorization(
        name: String,
        contextualName: String
): (suspend ()->R?) -> (suspend ()->R?){
    return {block ->
        {
            println("Intercepting with authentication $name and $contextualName")
            block()
        }
    }
}

inline fun <R> ((suspend ()->R?) -> (suspend ()->R?)).and(crossinline combinator: (suspend ()->R?) -> (suspend ()->R?)):  (suspend ()->R?) -> (suspend ()->R?){
    return  {block -> this(combinator(block))}
}

suspend fun <R> ((suspend ()->R?) -> (suspend ()->R?)).intercept(block: suspend ()->R?): R?{
    return this(block)()
}