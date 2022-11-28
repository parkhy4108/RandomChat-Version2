package com.dev_musashi.ranchat.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.dev_musashi.ranchat.presentation.ui.theme.RanChatTheme
import com.dev_musashi.ranchat.util.Screen
import com.dev_musashi.ranchat.util.graph
import com.dev_musashi.ranchat.util.rememberAppState

@Composable
fun Contents(){
    RanChatTheme {
        val appState = rememberAppState()
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = it,
                        modifier = Modifier,
                        snackbar = { snackBarData ->
                            Snackbar(snackBarData)
                        }
                    )
                },
                scaffoldState = appState.scaffoldState
            ) { innerPadding ->
                NavHost(
                    navController = appState.navController,
                    startDestination = Screen.SplashScreen.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    graph(appState = appState)
                }
            }
        }
    }
}