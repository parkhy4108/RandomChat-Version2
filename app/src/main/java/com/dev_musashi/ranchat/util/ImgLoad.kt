package com.dev_musashi.ranchat.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dev_musashi.ranchat.module.GlideApp
import com.dev_musashi.ranchat.R.drawable as AppImg

@Composable
fun ImgLoad(
    imgUrl: Uri,
    modifier: Modifier = Modifier
) {
    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }

    GlideApp.with(LocalContext.current)
        .asBitmap()
        .load(imgUrl)
        .fitCenter()
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bitmap.value = resource
            }
            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })

    bitmap.value?.asImageBitmap()?.let {
        Image(
            bitmap = it,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = modifier
        )
    } ?: Image(
        painter = painterResource(id = AppImg.ic_error),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier
    )
}
