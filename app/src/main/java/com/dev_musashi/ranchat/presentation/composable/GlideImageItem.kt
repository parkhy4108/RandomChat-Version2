package com.dev_musashi.ranchat.presentation.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dev_musashi.ranchat.domain.MediaStoreImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GlideImageItem(
    isSelected: Boolean,
    model: MediaStoreImage,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contentScale: ContentScale
) {
    val borderColor =
        if (isSelected) Color.Yellow else Color.White

    GlideImage(
        model = model.contentUri,
        contentDescription = null,
        modifier = modifier
            .border(2.dp, borderColor, RectangleShape)
            .clickable {
                onClick()
            },
        contentScale = contentScale
    )
}