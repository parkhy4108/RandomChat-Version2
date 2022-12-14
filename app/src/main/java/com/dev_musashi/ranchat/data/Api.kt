package com.dev_musashi.ranchat.data

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Api {
    @Multipart
    @POST("/upload")
    fun uploadImage(
        @Part image: MultipartBody.Part
    ) : Call<Result>
}