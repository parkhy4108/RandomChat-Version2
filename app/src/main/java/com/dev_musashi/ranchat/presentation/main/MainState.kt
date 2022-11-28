package com.dev_musashi.ranchat.presentation.main

data class MainState(
    val nickName: String = "",
    val chattingOkay: Boolean = false,
    val nicknameCheck: Boolean = false,
    val textField: Boolean = true
)
