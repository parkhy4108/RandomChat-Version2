package com.dev_musashi.ranchat.util

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SocketController {
    lateinit var nickName: String
    private const val uriDevice = "http://10.0.2.2:3004"
    private const val uriHome = "http://192.168.123.104:3004"

    val mSocket: Socket = IO.socket(uriHome)

    fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                mSocket.connect()
            } catch (e: Exception) {
                SnackBarManager.showMessage("앱을 다시 실행 해주시길 바랍니다.")
            }
        }
    }

    fun disconnect(){
        mSocket.emit("finish", SocketController.nickName)
    }

}
