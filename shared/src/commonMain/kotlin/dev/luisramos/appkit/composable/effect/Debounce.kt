package dev.luisramos.appkit.composable.effect

import dev.luisramos.appkit.composable.Effect
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration

fun <Output> Effect<Output>.debounce(id: Any, dueTime: Duration): Effect<Output> =
    flowOf(Unit)
        .onEach { delay(dueTime) }
        .flatMapConcat { this@debounce }
        .cancellable(id, cancelInFlight = true)
