package app.luisramos.ler


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.luisramos.ler.app.App
import app.luisramos.ler.app.toFeedsStore
import app.luisramos.ler.app.toLandingStore
import dev.luisramos.appkit.collectAsStateLifecycleAware
import dev.luisramos.appkit.composable.Store
import dev.luisramos.appkit.ui.WithOptionalStore

@Composable
fun ContentView(store: Store<App.State, App.Action>) {
    val navController = rememberNavController()
    val state by store.state.collectAsStateLifecycleAware()

    LaunchedEffect(state.route) {
        snapshotFlow { state.route }
            .collect { route ->
                when (route.navString) {
                    "feedList" -> navController.navigate(route.navString) {
                        popUpTo(route.navString) { inclusive = true }
                    }

                    else -> navController.navigate(route.navString)
                }
            }
    }

    NavHost(
        navController = navController,
        startDestination = "landing"
    ) {
        composable("landing") {
            WithOptionalStore(store.toLandingStore()) {
                LandingView(it)
            }
        }
        composable("feedList") {
            WithOptionalStore(store.toFeedsStore()) {
                FeedListView(it)
            }
        }
    }
}






