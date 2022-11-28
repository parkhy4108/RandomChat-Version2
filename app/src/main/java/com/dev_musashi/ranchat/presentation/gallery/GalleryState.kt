package com.dev_musashi.ranchat.presentation.gallery

import androidx.compose.runtime.mutableStateMapOf
import com.dev_musashi.ranchat.domain.MediaStoreImage

data class GalleryState(
    val init: Boolean = true,
    val images : List<MediaStoreImage> = emptyList(),
    val imageUri: String = "",
    val isSelectedState: MutableMap<Int, Boolean> = mutableStateMapOf(),
)