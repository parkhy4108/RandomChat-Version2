package com.dev_musashi.ranchat.presentation.chatting

import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateMapOf
import com.dev_musashi.ranchat.domain.MediaStoreImage
import com.dev_musashi.ranchat.domain.Message

data class ChattingState  (
    val init: Boolean = true,
    val nickName: String = "",
    val comment: String = "",
    val messageList: List<Message> = emptyList(),
    val textField: Boolean = true,
    val refresh: Boolean = false,
    val imageUri: String = "",
    val bottomView: Boolean = false,
    val galleryImages: List<MediaStoreImage> = emptyList(),
    val isSelectedState: MutableMap<Int, Boolean> = mutableStateMapOf(),
    val openDialog: Boolean = false
)

