package com.github.distiya.first_service

import mu.KotlinLogging
import org.slf4j.Logger

private val functionInterceptorLogger: Logger = KotlinLogging.logger{}

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
        userRoles: String,
        allowedRoles: List<String>
): (suspend ()->R?) -> (suspend ()->R?){
    return {block ->
        {
            val isAllowed = userRoles.split(",").any {
                allowedRoles.contains(it)
            }
            if(isAllowed){
                block()
            }
            else{
                functionInterceptorLogger.error("User is not authorized to perform the action")
                throw RuntimeException("Not authorized")
            }
        }
    }
}

fun <R> ((suspend ()->R?) -> (suspend ()->R?)).andWithTracing(
        name: String,
        contextualName: String
):  (suspend ()->R?) -> (suspend ()->R?){
    return  {block -> this(withTracing<R>(name,contextualName)(block))}
}

fun <R> ((suspend ()->R?) -> (suspend ()->R?)).andWithAuthorization(
        userRoles: String,
        allowedRoles: List<String>
):  (suspend ()->R?) -> (suspend ()->R?){
    return  {block -> this(withAuthorization<R>(userRoles,allowedRoles)(block))}
}

suspend fun <R> ((suspend ()->R?) -> (suspend ()->R?)).intercept(block: suspend ()->R?): R?{
    return this(block)()
}