package dev.luisramos.appkit.composable

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

/**
 * Effect is a Flow, the type alias allows for us decent semantics at call site
 * keeping all the flow super powers
 */
typealias Effect<T> = Flow<T>

fun <Output> effectOf(vararg elements: Output): Effect<Output> = flowOf(*elements)

fun <Output> effectOf(value: Output): Effect<Output> = flowOf(value)

fun <Output> effect(block: suspend FlowCollector<Output>.() -> Unit): Effect<Output> =
    flow(block)

fun <Output> effectOf(block: suspend () -> Output): Effect<Output> = flow { emit(block()) }

fun <Output> emptyEffect(): Effect<Output> = emptyFlow()

fun <Output> noEffect(): Effect<Output> = emptyFlow()

fun <Output> concatenate(vararg effects: Effect<Output>): Effect<Output> {
    if (effects.isEmpty()) return noEffect()
    return flowOf(*effects).flattenConcat()
}

fun <Output> merge(vararg effects: Effect<Output>): Effect<Output> {
    if (effects.isEmpty()) return noEffect()
    return flowOf(*effects).flattenMerge()
}

/**
 * Does some computation and emits an empty flow.
 */
fun <Output> fireAndForget(block: suspend () -> Unit): Effect<Output> = flow { block() }

/**
 * Allows us to fire an effect and cast it to Effect type
 */
fun <Output, NewOutput> Flow<Output>.fireAndForget(): Effect<NewOutput> =
    flatMapConcat { emptyFlow() }
