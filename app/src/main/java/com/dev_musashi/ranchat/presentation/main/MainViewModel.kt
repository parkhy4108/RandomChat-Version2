package com.dev_musashi.ranchat.presentation.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dev_musashi.ranchat.util.Screen
import com.dev_musashi.ranchat.util.SnackBarManager
import com.dev_musashi.ranchat.util.SocketController
import io.socket.emitter.Emitter
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    var state = mutableStateOf(MainState())
        private set

    private val mSocket = SocketController.mSocket
    private val nickname get() = state.value.nickName
    private val nicknameCheckResult: Emitter.Listener = Emitter.Listener {
        if (it[0] == false) {
            SnackBarManager.showMessage("존재하는 닉네임입니다.")
        } else {
            state.value = state.value.copy(chattingOkay = true, nicknameCheck = false, textField = false)
            SnackBarManager.showMessage("닉네임 설정이 완료되었습니다.")
            SocketController.nickName = nickname
        }
    }

    private val connectionResult : Emitter.Listener = Emitter.Listener {
        if(it.isNotEmpty()) {
            SnackBarManager.showMessage("연결이 완료되었습니다.")
        }
    }

    init {
        mSocket.on("nicknameCheckResult", nicknameCheckResult)
        mSocket.on("connectionResult", connectionResult)
        if(!mSocket.connected()) {
            SnackBarManager.showMessage("잠시만 기다려주세요.")
        }
        state.value = state.value.copy(textField = true)
    }

    fun onValueChanged(newValue: String) {
        if(newValue == "") {
            state.value = state.value.copy(nickName = newValue, nicknameCheck = false)
        } else {
            state.value = state.value.copy(nickName = newValue, nicknameCheck = true)
        }
    }

    fun nicknameCheck() {
        if(SocketController.mSocket.connected()) {
            mSocket.emit("nicknameCheck", nickname)
        } else {
            SnackBarManager.showMessage("죄송합니다. 현재 서버와의 연결이 원활하지 않습니다. 잠시후 다시 시도 해주시길 바랍니다.")
        }
    }

    fun open(open: (String) -> Unit) {
        open(Screen.ChattingRoom.passNickname(nickname = nickname))
    }
}