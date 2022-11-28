package com.dev_musashi.ranchat.domain

import com.dev_musashi.ranchat.data.Result
import retrofit2.Call
import java.net.URI


interface Repository {
    suspend fun uploadImage(imageUri: String, roomName: String, nickname: String, onResult: (Throwable?)->Unit)
}