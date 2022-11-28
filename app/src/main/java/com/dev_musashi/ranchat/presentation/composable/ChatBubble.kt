package com.dev_musashi.ranchat.presentation.composable

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dev_musashi.ranchat.domain.Message

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChatBubble(
    item: Message,
    color: Color,
    onImageTouch: (String)->Unit
) {
    Text(
        text = item.nickname,
        color = color,
        modifier = Modifier
            .padding(8.dp, 0.dp)
    )
    Card(
        backgroundColor = color,
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(8.dp, 0.dp),
        elevation = 7.dp
    ) {
        if (
            (item.comment.contains(".jpg") || item.comment.contains(".png")
                    || item.comment.contains(".gif") || item.comment.contains(".bmp"))
            && item.comment.length == 57
        ) {
            val image = item.comment
            Box(modifier = Modifier
                .width(280.dp)
                .height(350.dp), contentAlignment = Alignment.Center
            ) {
                GlideImage(
                    model = image.toUri(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onImageTouch(image) },
                    contentScale = ContentScale.FillBounds,
                )
            }
        } else {
            Text(
                text = item.comment,
                color = Color.Black,
                modifier = Modifier
                    .padding(15.dp, 10.dp),
                fontSize = 15.sp
            )
        }

    }
}