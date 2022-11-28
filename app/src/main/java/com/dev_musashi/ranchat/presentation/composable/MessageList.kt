package com.dev_musashi.ranchat.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dev_musashi.ranchat.domain.Message

@Composable
fun MessageList(
    item: Message,
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal,
    color: Color,
    onClick: (String) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = alignment
    ) {
        ChatBubble(item = item, color = color, onImageTouch = { onClick(it) })
    }
}