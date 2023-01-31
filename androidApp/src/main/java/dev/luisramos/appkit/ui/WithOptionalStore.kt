package dev.luisramos.appkit.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import dev.luisramos.appkit.collectAsStateLifecycleAware
import dev.luisramos.appkit.composable.Store

@Composable
fun <State, Action> WithOptionalStore(
    store: Store<State?, Action>,
    content: @Composable (Store<State, Action>) -> Unit
) {
    val state by store.state.collectAsStateLifecycleAware()

    state?.let {
        val newStore = store.scope<State> { scopedState -> scopedState ?: it }
        content(newStore)
    }
}
