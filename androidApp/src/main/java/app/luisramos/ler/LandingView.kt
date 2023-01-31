package app.luisramos.ler

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import app.luisramos.ler.landing.Landing
import app.luisramos.ler.landing.LandingStore
import dev.luisramos.appkit.collectAsStateLifecycleAware

@Composable
fun LandingView(store: LandingStore) {
    val state by store.state.collectAsStateLifecycleAware()

    LaunchedEffect(Unit) {
        store.send(Landing.Action.OnAppear)
    }

    Box(Modifier.fillMaxSize()) {
        Text(
            "Hello there. isLoading = ${state.isLoading}",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}