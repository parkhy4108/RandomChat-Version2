package com.dev_musashi.ranchat.util

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dev_musashi.ranchat.presentation.chatting.ChattingScreen
import com.dev_musashi.ranchat.presentation.gallery.GalleryScreen
import com.dev_musashi.ranchat.presentation.image.ImageScreen
import com.dev_musashi.ranchat.presentation.main.MainScreen
import com.dev_musashi.ranchat.presentation.splash.SplashScreen

fun NavGraphBuilder.graph(appState: AppState) {

    composable(route = Screen.SplashScreen.route) {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(route = Screen.MainScreen.route) {
        MainScreen(open = { route -> appState.navigate(route) })
    }

    composable(route = Screen.ChattingRoom.route, arguments = listOf(navArgument(ARG_KEY) {
        type = NavType.StringType
        defaultValue = ""
    })) {
        ChattingScreen(nickname = it.arguments?.getString(ARG_KEY)!!,
            popUp = { appState.popUp() },
            open = { route -> appState.navigate(route) })
    }

    composable(
        route = Screen.ImageScreen.route, arguments = listOf(
            navArgument(ARG_IMAGE) {
                type = NavType.StringType
                defaultValue = ""
            },
        )
    ) {
        val image = it.arguments?.getString(ARG_IMAGE)
        ImageScreen(image = image!!, popUp = { appState.popUp() })
    }

    composable(route = Screen.GalleryScreen.route, arguments = listOf(navArgument(ARG_NICKNAME) {
        type = NavType.StringType
        defaultValue = ""
    }, navArgument(ARG_ROOMNAME) {
        type = NavType.StringType
        defaultValue = ""
    })) {
        val nickname = it.arguments?.getString(ARG_NICKNAME)
        val roomName = it.arguments?.getString(ARG_ROOMNAME)
        GalleryScreen(
            nickname = nickname!!,
            roomName = roomName!!,
            open = { route -> appState.navigate(route) },
            popUp = { appState.popUp() },
        )
    }
}