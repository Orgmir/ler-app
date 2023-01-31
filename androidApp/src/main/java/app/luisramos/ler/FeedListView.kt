package app.luisramos.ler

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import app.luisramos.ler.feeds.Feeds
import dev.luisramos.appkit.collectAsStateLifecycleAware
import dev.luisramos.appkit.composable.Store

@Composable
fun FeedListView(store: Store<Feeds.State, Feeds.Action>) {
    val state by store.state.collectAsStateLifecycleAware()

    Box(Modifier.fillMaxSize()) {
        Text(
            "Feeds: ${state.feeds}",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}
