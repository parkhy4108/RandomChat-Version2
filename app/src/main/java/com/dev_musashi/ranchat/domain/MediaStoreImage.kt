package com.dev_musashi.ranchat.domain

import android.net.Uri
import java.util.*

data class MediaStoreImage(
    val id: Long,
    val displayName: String,
    val dateTaken: Date,
    val contentUri: Uri,
    val path: String,
)