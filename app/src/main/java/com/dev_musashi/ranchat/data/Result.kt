package com.dev_musashi.ranchat.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("result")
    @Expose
    val result : Int,

    @SerializedName("imageUri")
    @Expose
    val imageUri: String
)