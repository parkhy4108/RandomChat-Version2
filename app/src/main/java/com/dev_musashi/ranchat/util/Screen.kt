package com.dev_musashi.ranchat.util

import android.util.Log

const val ARG_KEY = "id"
const val ARG_IMAGE = "image"
const val ARG_NICKNAME = "nickname"
const val ARG_ROOMNAME = "roomName"

sealed class Screen(val route: String) {
    object SplashScreen : Screen(route = "SPLASH")
    object MainScreen : Screen(route = "MAIN")
    object ChattingRoom : Screen(route = "CHATTING?nickname={$ARG_KEY}")
    object ImageScreen : Screen(route = "IMAGE?image={$ARG_IMAGE}")
    object GalleryScreen : Screen(route = "GALLERY?nickname={$ARG_NICKNAME}&roomName={$ARG_ROOMNAME}")

    fun passNickname(nickname: String = ""): String {
        return "CHATTING?nickname=$nickname"
    }

    fun passImage(image: String = ""): String {
        Log.d("TAG", "passImage: $image")
        return "IMAGE?image=$image"
    }

    fun passNicknameAndRoomName(
        nickname: String,
        roomName: String
    ) : String {
        return "GALLERY?nickname=$nickname&roomName=$roomName"
    }
}