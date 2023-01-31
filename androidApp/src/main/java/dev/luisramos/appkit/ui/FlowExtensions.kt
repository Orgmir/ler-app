package dev.luisramos.appkit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Ensures the flow is collected in only after the activity
 * lifecycle is in the started state, and it will stop collecting
 * if the lifecycle dies. The [StateFlow.value] is used has the
 * initial value.
 */
// compose.collectAsState() does the same suppression, without it we have signature clashes
@Suppress("StateFlowValueCalledInComposition")
@Composable
fun <T> StateFlow<T>.collectAsStateLifecycleAware(): State<T> {
    val lifecycleOwner = LocalLifecycleOwner.current
    return remember(this, lifecycleOwner) {
        flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .distinctUntilChanged { old, new -> old == new }
    }
        .collectAsState(value)
}
