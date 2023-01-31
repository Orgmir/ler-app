package dev.luisramos.composable

import dev.luisramos.appkit.composable.reducer.CombineReducer
import dev.luisramos.appkit.composable.reducer.ReducerResult
import dev.luisramos.appkit.composable.reducer.withNoEffect
import kotlin.test.Test
import kotlin.test.assertEquals

class ReducerTest {
    private class Counter : Reducer<Counter.State, Counter.Action> {
        data class State(
            val counter: Int
        )

        sealed class Action {
            object Increment : Action()
            object Decrement : Action()
        }

        override fun reduce(state: State, action: Action): ReducerResult<State, Action> =
            when (action) {
                Action.Decrement ->
                    state.copy(counter = state.counter - 1)
                        .withNoEffect()
                Action.Increment ->
                    state.copy(counter = state.counter + 1)
                        .withNoEffect()
            }
    }

    @Test
    fun combineShouldUpdateStateCorrectly() {
        val combinedReducers = CombineReducer {
            +Counter()
            +Counter()
        }

        val (newState, _) = combinedReducers.reduce(Counter.State(0), Counter.Action.Increment)

        assertEquals(newState.counter, 2)
    }
}
