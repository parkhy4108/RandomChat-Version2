package com.dev_musashi.ranchat.util

import android.content.res.Resources
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dev_musashi.ranchat.util.SnackBarMessage.Companion.toMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    resources: Resources = resources(),
    snackBarManager: SnackBarManager = SnackBarManager,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(scaffoldState, navController, snackBarManager, resources, coroutineScope) {
    AppState(scaffoldState, navController, snackBarManager, resources, coroutineScope)
}

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@Stable
class AppState constructor(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val snackBarManager: SnackBarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {

    init {
        coroutineScope.launch {
            val text = snackBarManager.snackMessage.value.toString()
            snackBarManager.snackMessage.collect { messages ->
                if (messages != null && text != messages.toString()) {
                    scaffoldState.snackbarHostState.showSnackbar(messages.toMessage(resources))
                }
            }
        }
    }



    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    private val NavGraph.startDestination: NavDestination?
        get() = findNode(startDestinationId)

    private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
        return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
    }
}
