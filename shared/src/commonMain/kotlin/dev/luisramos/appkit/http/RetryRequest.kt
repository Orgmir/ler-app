package dev.luisramos.appkit.http

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

suspend fun <T> retryWhen(
    predicate: suspend (result: T, attempt: Long) -> Boolean,
    block: suspend () -> T
): T {
    var attempt = 0L
    var shallRetry: Boolean
    var result: T
    do {
        currentCoroutineContext().ensureActive()
        result = block()
        if (predicate(result, attempt)) {
            shallRetry = true
            attempt++
        } else {
            shallRetry = false
        }
    } while (shallRetry)
    return result
}

suspend fun <T> retry(
    retries: Long = Long.MAX_VALUE,
    predicate: suspend (result: Result<T>) -> Boolean = { it.isFailure },
    block: suspend () -> Result<T>
): Result<T> = retryWhen({ result, attempt -> attempt < retries && predicate(result) }, block)

/**
 * Retries the block based on predicate, and delays it
 */
suspend fun <T> retryWithDelay(
    retries: Long = Long.MAX_VALUE,
    initialDelay: Duration = 0.1.seconds,
    maxDelay: Duration = 15.0.seconds,
    factor: Double = 2.0,
    predicate: suspend (result: Result<T>) -> Boolean = { it.isFailure },
    block: suspend () -> Result<T>
): Result<T> {
    var currentDelayInMillis = initialDelay.toLong(DurationUnit.MILLISECONDS)
    val maxDelayInMillis = maxDelay.toLong(DurationUnit.MILLISECONDS)
    return retryWhen(
        predicate = { result, attempt ->
            attempt < retries && predicate(result).also {
                if (it) {
                    delay(currentDelayInMillis)
                    currentDelayInMillis =
                        (currentDelayInMillis * factor).toLong().coerceAtMost(maxDelayInMillis)
                }
            }
        },
        block = block
    )
}

fun <T> Flow<Result<T>>.retryWhen(
    predicate: suspend FlowCollector<Result<T>>.(result: Result<T>, attempt: Long) -> Boolean
): Flow<Result<T>> = flow {
    var attempt = 0L
    var shallRetry: Boolean
    do {
        currentCoroutineContext().ensureActive()
        shallRetry = false
        collect { result ->
            if (predicate(result, attempt)) {
                shallRetry = true
                attempt++
            } else {
                shallRetry = false
                emit(result)
            }
        }
    } while (shallRetry)
}

/**
 * Retries the flow based on predicate
 */
fun <T> Flow<Result<T>>.retry(
    retries: Long = Long.MAX_VALUE,
    predicate: suspend (result: Result<T>) -> Boolean = { it.isFailure }
): Flow<Result<T>> = retryWhen { result, attempt -> attempt < retries && predicate(result) }

/**
 * Retries the flow based on predicate, and delays it
 */
fun <T> Flow<Result<T>>.retryWithDelay(
    retries: Long = Long.MAX_VALUE,
    initialDelay: Duration = 0.1.seconds,
    maxDelay: Duration = 15.0.seconds,
    factor: Double = 2.0,
    predicate: suspend (result: Result<T>) -> Boolean = { it.isFailure }
): Flow<Result<T>> {
    var currentDelayInMillis = initialDelay.toLong(DurationUnit.MILLISECONDS)
    val maxDelayInMillis = maxDelay.toLong(DurationUnit.MILLISECONDS)
    return retryWhen { result, attempt ->
        attempt < retries && predicate(result).also {
            if (it) {
                delay(currentDelayInMillis)
                currentDelayInMillis =
                    (currentDelayInMillis * factor).toLong().coerceAtMost(maxDelayInMillis)
            }
        }
    }
}
