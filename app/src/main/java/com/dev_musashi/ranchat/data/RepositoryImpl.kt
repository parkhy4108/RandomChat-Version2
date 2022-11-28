package com.dev_musashi.ranchat.data

import android.content.Context
import com.dev_musashi.ranchat.domain.Message
import com.dev_musashi.ranchat.domain.Repository
import com.dev_musashi.ranchat.util.SocketController
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val api: Api,
    private val context: Context
) : Repository {

    private val _context = context
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()



    override suspend fun uploadImage(imageUri: String, roomName: String, nickname: String, onResult: (Throwable?)->Unit) {
        val file = File(imageUri)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body : MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestFile)
        api.uploadImage(body).enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val result = response.body()?.result
                val uri = response.body()?.imageUri
                if(result == 1) {
                    CoroutineScope(Dispatchers.Default).launch {
                        if (uri != null) {
                            sendImage(imageUri = uri, roomName = roomName, nickname = nickname)
                        }
                        onResult(null)
                    }
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                onResult(t)
            }
        })
    }

    private suspend fun sendImage(imageUri: String, roomName: String, nickname: String) {
        val messageGson = Message(nickname = nickname, comment = imageUri)
        val messageJson = gson.toJson(messageGson)
        val data = JSONObject()
        data.put("roomName", roomName)
        data.put("message", messageJson)
        SocketController.mSocket.emit("newImage", data)
    }

}

