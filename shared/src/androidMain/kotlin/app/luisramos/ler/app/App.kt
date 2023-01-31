package app.luisramos.ler.app

import app.luisramos.ler.landing.Landing.Action as LandingAction
import app.luisramos.ler.landing.Landing.State as LandingState
import app.luisramos.ler.feeds.Feeds
import app.luisramos.ler.landing.Landing
import dev.luisramos.appkit.composable.ActionPath
import dev.luisramos.appkit.composable.Reducer
import dev.luisramos.appkit.composable.StatePath
import dev.luisramos.appkit.composable.Store
import dev.luisramos.appkit.composable.reducer.Combine
import dev.luisramos.appkit.composable.reducer.OptionalScope
import dev.luisramos.appkit.composable.reducer.withNoEffect
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class App : Reducer<App.State, App.Action> {
    data class State(
        val route: Route = Route.Landing()
    ) {
        companion object {
            val landing = StatePath<State, LandingState?>(
                embed = { global, local ->
                    global.copy(route = local?.let { Route.Landing(it) } ?: global.route)
                },
                extract = { (it.route as? Route.Landing)?.state }
            )
            val feedList = StatePath<State, Feeds.State?>(
                embed = { global, local ->
                    global.copy(route = local?.let { Route.FeedList(it) } ?: global.route)
                },
                extract = { (it.route as? Route.FeedList)?.state }
            )
        }
    }

    sealed class Route {
        data class Landing(val state: LandingState = LandingState()) : Route()
        data class FeedList(val state: Feeds.State = Feeds.State()) : Route()

        val navString: String
            get() = when (this) {
                is Landing -> "landing"
                is FeedList -> "feedList"
            }
    }

    sealed class Action {
        data class Landing(val action: LandingAction) : Action()
        data class FeedList(val action: Feeds.Action) : Action()

        companion object {
            val landing = ActionPath<Action, Landing, LandingAction>(::Landing, Landing::action)
            val feedList = ActionPath<Action, FeedList, Feeds.Action>(::FeedList, FeedList::action)
        }
    }

    override val body: Reducer<State, Action> = Combine {
        +OptionalScope(State.landing, Action.landing) {
            Landing()
        }
        +OptionalScope(State.feedList, Action.feedList) {
            Feeds()
        }

        +Reducer<State, Action> { state, action ->
            when (action) {
                is Action.Landing -> when (action.action) {
                    Landing.Action.OnAppear ->
                        state.copy(route = Route.FeedList()).withNoEffect()
                }

                is Action.FeedList -> state.withNoEffect()
            }
        }
    }
}

fun Store<App.State, App.Action>.toLandingStore(): Store<LandingState?, LandingAction> =
    scope(
        toLocalState = { (it.route as? App.Route.Landing)?.state },
        fromLocalAction = { App.Action.Landing(it) }
    )

fun Store<App.State, App.Action>.toFeedsStore(): Store<Feeds.State?, Feeds.Action> =
    scope(
        toLocalState = { (it.route as? App.Route.FeedList)?.state },
        fromLocalAction = { App.Action.FeedList(it) }
    )

/**
 * Helper so we build a Store for the AppReducer with default params.
 *
 * Also helps with K/N, since default params don't work over the bridge
 */
fun AppStore(): Store<App.State, App.Action> = AppStore(Dispatchers.Main)

/**
 * For Android, we need the store to use the coroutine context of a Compose Scope,
 * so the updates run in their own ui thread
 */
fun AppStore(coroutineContext: CoroutineContext): Store<App.State, App.Action> =
    Store(
        initialState = App.State(),
        reducer = App(),
        coroutineContext = coroutineContext
    )