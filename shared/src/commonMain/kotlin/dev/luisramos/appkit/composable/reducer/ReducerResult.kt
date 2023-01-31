package dev.luisramos.appkit.composable.reducer

import dev.luisramos.appkit.composable.Effect
import dev.luisramos.appkit.composable.effectOf
import dev.luisramos.appkit.composable.emptyEffect
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

data class ReducerResult<out State, Action>(
    val state: State,
    val effect: Effect<Action>
)

infix fun <State, Action> State.withEffect(block: suspend FlowCollector<Action>.() -> Unit): ReducerResult<State, Action> =
    ReducerResult(this, flow(block))

fun <State, Action> State.withEffectOf(vararg elements: Action): ReducerResult<State, Action> =
    ReducerResult(this, effectOf(*elements))

fun <State, Action> State.withEffectOf(action: Action): ReducerResult<State, Action> =
    ReducerResult(this, effectOf(action))

infix fun <State, Action> State.withEffectOf(action: suspend () -> Action): ReducerResult<State, Action> =
    ReducerResult(this, effectOf(action))

fun <State, Action> State.withNoEffect(): ReducerResult<State, Action> =
    ReducerResult(this, emptyEffect())

infix fun <State, Action> State.withEffect(effect: Effect<Action>): ReducerResult<State, Action> =
    ReducerResult(this, effect)
