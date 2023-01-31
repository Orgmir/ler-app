@file:Suppress("FunctionName")

package dev.luisramos.appkit.composable.reducer

import dev.luisramos.appkit.composable.Effect
import dev.luisramos.appkit.composable.Reducer
import dev.luisramos.appkit.composable.concatenate

class CombineReducer<State, Action>(
    block: CombineReducer<State, Action>.() -> Unit
) : Reducer<State, Action> {

    private var reducers = arrayListOf<Reducer<State, Action>>()

    init {
        block()
        check(reducers.isNotEmpty()) { "Created a CombineReducer with no reducers added in its builder." }
    }

    override fun reduce(state: State, action: Action): ReducerResult<State, Action> {
        var currState = state
        var effects = emptyArray<Effect<Action>>()
        reducers.forEach {
            val (newState, newEffect) = it.reduce(currState, action)
            currState = newState
            effects += newEffect
        }
        return currState.withEffect(concatenate(*effects))
    }

    operator fun Reducer<State, Action>.unaryPlus() {
        reducers += this
    }
}

fun <State, Action> Combine(
    block: CombineReducer<State, Action>.() -> Unit
) = CombineReducer(block)
