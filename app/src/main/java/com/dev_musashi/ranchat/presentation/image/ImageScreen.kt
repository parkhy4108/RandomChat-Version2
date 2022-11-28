package com.dev_musashi.ranchat.presentation.image

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dev_musashi.ranchat.presentation.composable.CustomIconButton
import com.dev_musashi.ranchat.R.drawable as AppImg

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageScreen(
    image: String,
    popUp: () -> Unit,
    viewModel: ImageViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
            ) {
                GlideImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                CustomIconButton(
                    onClick = { popUp() },
                    icon = AppImg.ic_back,
                    tint = Color.White
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                CustomIconButton(
                    onClick = {
                        viewModel.onImageSaveButtonClicked(uri = image)
                    },
                    icon = AppImg.ic_save,
                    tint = Color.White
                )
            }
        }

    }
}