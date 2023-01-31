@file:Suppress("FunctionName")

package dev.luisramos.appkit.composable.reducer

import dev.luisramos.appkit.composable.ActionPath
import dev.luisramos.appkit.composable.Reducer
import dev.luisramos.appkit.composable.StatePath
import kotlinx.coroutines.flow.map

class ScopeReducer<State, Action, ChildState, ChildAction>(
    private val toChildState: (State) -> ChildState,
    private val fromChildState: (State, ChildState) -> State,
    private val toChildAction: (Action) -> ChildAction?,
    private val fromChildAction: (ChildAction) -> Action,
    private val childReducer: Reducer<ChildState, ChildAction>
) : Reducer<State, Action> {
    override fun reduce(state: State, action: Action): ReducerResult<State, Action> {
        val childAction = toChildAction(action) ?: return state.withNoEffect()
        val childState = toChildState(state)
        val (newChildState, newChildEffect) = childReducer.reduce(childState, childAction)
        return fromChildState(state, newChildState).withEffect(
            newChildEffect.map { fromChildAction(it) }
        )
    }
}

fun <State, Action, ChildState, ChildAction> Scope(
    toChildState: (State) -> ChildState,
    fromChildState: (State, ChildState) -> State,
    toChildAction: (Action) -> ChildAction?,
    fromChildAction: (ChildAction) -> Action,
    childReducer: () -> Reducer<ChildState, ChildAction>
): Reducer<State, Action> =
    ScopeReducer(toChildState, fromChildState, toChildAction, fromChildAction, childReducer())

fun <State, Action, ChildState, ChildAction> Scope(
    state: StatePath<State, ChildState>,
    action: ActionPath<Action, ChildAction>,
    childReducer: () -> Reducer<ChildState, ChildAction>
): Reducer<State, Action> = Scope(
    toChildState = state::extract,
    fromChildState = state::embed,
    toChildAction = action::extract,
    fromChildAction = action::embed,
    childReducer = childReducer
)
