package dev.luisramos.appkit.composable.reducer

import dev.luisramos.appkit.composable.Effect
import dev.luisramos.appkit.composable.Reducer
import dev.luisramos.appkit.composable.fireAndForget
import dev.luisramos.appkit.composable.noEffect
import kotlinx.coroutines.flow.map

interface LifecycleReducer<WrappedState, WrappedAction> :
    Reducer<WrappedState?, LifecycleReducer.Action<WrappedAction>> {
    sealed class Action<out Wrapped> {
        object OnAppear : Action<Nothing>()
        object OnDisappear : Action<Nothing>()
        data class Wrapped<Wrapped>(val action: Wrapped) : Action<Wrapped>()
    }
}

internal class LifecycleReducerIml<WrappedState, WrappedAction>(
    private val wrapped: Reducer<WrappedState & Any, WrappedAction>,
    private val onAppear: () -> Effect<WrappedAction>,
    private val onDisappear: () -> Effect<Unit>
) : LifecycleReducer<WrappedState, WrappedAction> {

    override val body = Combine {
        +OptionalScope<WrappedState?, LifecycleReducer.Action<WrappedAction>, WrappedState & Any, WrappedAction>(
            toChildState = { it },
            fromChildState = { _, child -> child },
            toChildAction = { (it as? LifecycleReducer.Action.Wrapped<WrappedAction>)?.action },
            fromChildAction = { LifecycleReducer.Action.Wrapped(it) }
        ) {
            wrapped
        }
        +Reducer<WrappedState?, LifecycleReducer.Action<WrappedAction>> { state, action ->
            when (action) {
                LifecycleReducer.Action.OnAppear ->
                    state withEffect onAppear().map { LifecycleReducer.Action.Wrapped(it) }

                LifecycleReducer.Action.OnDisappear ->
                    state withEffect onDisappear().fireAndForget()

                is LifecycleReducer.Action.Wrapped<WrappedAction> ->
                    state.withNoEffect()
            }
        }
    }
}

fun <State, Action> WithLifecycle(
    onAppear: () -> Effect<Action>,
    onDisappear: () -> Effect<Unit> = { noEffect() },
    block: CombineReducer<State & Any, Action>.() -> Unit
): LifecycleReducer<State, Action> =
    LifecycleReducerIml(CombineReducer(block), onAppear, onDisappear)

fun <State, Action> WithLifecycle(
    onAppear: Effect<Action>,
    onDisappear: Effect<Unit> = noEffect(),
    block: CombineReducer<State & Any, Action>.() -> Unit
): LifecycleReducer<State, Action> =
    LifecycleReducerIml(CombineReducer(block), { onAppear }, { onDisappear })
