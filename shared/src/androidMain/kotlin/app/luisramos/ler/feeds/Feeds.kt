package app.luisramos.ler.feeds

import dev.luisramos.appkit.composable.Reducer
import dev.luisramos.appkit.composable.reducer.ReducerResult
import dev.luisramos.appkit.composable.reducer.withNoEffect

class Feeds : Reducer<Feeds.State, Feeds.Action> {
    data class State(
        val feeds: List<String> = listOf("Feed #1")
    )

    sealed class Action {
        object OnAppear : Action()
    }

    override fun reduce(state: State, action: Action): ReducerResult<State, Action> =
        when (action) {
            // TODO: load existing feeds
            Action.OnAppear -> state.withNoEffect()
        }
}