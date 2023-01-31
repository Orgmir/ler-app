package app.luisramos.ler.landing

import dev.luisramos.appkit.composable.Reducer
import dev.luisramos.appkit.composable.Store
import dev.luisramos.appkit.composable.reducer.ReducerResult
import dev.luisramos.appkit.composable.reducer.withNoEffect

typealias LandingStore = Store<Landing.State, Landing.Action>

class Landing : Reducer<Landing.State, Landing.Action> {
    data class State(
        val isLoading: Boolean = true
    )

    sealed class Action {
        object OnAppear : Action()
    }

    override fun reduce(state: State, action: Action): ReducerResult<State, Action> =
        when (action) {
            Action.OnAppear -> state.copy(isLoading = false).withNoEffect()
        }
}