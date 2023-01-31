package dev.luisramos.appkit.composable

import dev.luisramos.appkit.composable.reducer.ReducerResult
import dev.luisramos.appkit.composable.reducer.withNoEffect

/**
 * Reducer protocol that enables abstracting away business logic in a single reducer
 * or in a combination of several reducers
 *
 * Check [dev.luisramos.composable.reducer.CombineReducer] to combine several reducers into one.
 */
interface Reducer<State, Action> {
    companion object {
        operator fun <State, Action> invoke(
            block: (state: State, action: Action) -> ReducerResult<State, Action>
        ): Reducer<State, Action> = object : Reducer<State, Action> {
            override fun reduce(state: State, action: Action): ReducerResult<State, Action> =
                block(state, action)
        }
    }

    fun reduce(state: State, action: Action): ReducerResult<State, Action> =
        body.reduce(state, action)

    /**
     * You can override this property instead of [reduce] if you want
     * to compose several reducers together
     */
    val body: Reducer<State, Action> get() = EmptyReducer()
}

/**
 * Reducer that does nothing
 *
 * Useful as a placeholder for APIs that hold reducers
 */
class EmptyReducer<State, Action> : Reducer<State, Action> {
    override fun reduce(state: State, action: Action): ReducerResult<State, Action> =
        state.withNoEffect()
}

/**
 * Describes how to extract an action from a parent action. This assumes actions
 * in the for of
 *
 * sealed class Action {
 *  data class Child(val action: ChildAction): Action()
 *
 *  companion object {
 *      val child = CasePath(::Child, Child::action)
 *  }
 * }
 */
class ActionPath<Root, Value>(
    embed: (Value) -> Root,
    extract: (Root) -> Value?
) {
    private val _embed = embed
    private val _extract = extract
    fun embed(value: Value): Root = _embed(value)
    fun extract(root: Root): Value? = _extract(root)
}

/**
 * Helper function to help us extract an action from a parent action
 * and embed an action into a parent action
 */
@Suppress("FunctionName")
inline fun <Root, reified Case : Root, Value> ActionPath(
    noinline embed: (Value) -> Root,
    crossinline extract: (Case) -> Value
): ActionPath<Root, Value> = ActionPath<Root, Value>(
    embed = embed,
    extract = { root -> (root as? Case)?.let(extract) }
)

/**
 * Describes how to extract state from parent state
 * and how to embed state into parent state
 */
class StatePath<Root, Value>(
    embed: (Root, Value) -> Root,
    extract: (Root) -> Value
) {
    private val _embed = embed
    private val _extract = extract
    fun embed(root: Root, value: Value): Root = _embed(root, value)
    fun extract(root: Root): Value = _extract(root)
}
