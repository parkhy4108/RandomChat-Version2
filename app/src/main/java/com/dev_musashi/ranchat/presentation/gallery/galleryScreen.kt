package com.dev_musashi.ranchat.presentation.gallery

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev_musashi.ranchat.presentation.composable.CustomIconButton
import com.dev_musashi.ranchat.presentation.composable.GlideImageItem
import com.dev_musashi.ranchat.R.drawable as AppImg

@Composable
fun GalleryScreen(
    nickname: String,
    roomName: String,
    open: (String)->Unit,
    popUp: () -> Unit,
    viewModel: GalleryViewModel = hiltViewModel()
) {
    val imageWidth = LocalConfiguration.current.screenWidthDp
    val imageHeight = imageWidth / 3
    val state by viewModel.state

    LaunchedEffect(Unit) {
        if (state.init) {
            viewModel.init(nickname, roomName)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomIconButton(
                onClick = { viewModel.popUp(popUp) },
                icon = AppImg.ic_back,
                tint = Color.Black
            )
            CustomIconButton(
                onClick = { viewModel.onImageSend(popUp) },
                icon = AppImg.ic_send,
                tint = Color.Black
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 2.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            itemsIndexed(state.images) { index, image ->
                Box(modifier = Modifier.fillMaxSize()) {
                    GlideImageItem(
                        isSelected = state.isSelectedState[index] ?: false,
                        model = image,
                        modifier = Modifier
                            .height(imageHeight.dp),
                        onClick = { viewModel.onImageTouch(image = image.contentUri.toString(), open) },
                        contentScale = ContentScale.Crop
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        CircleIconButton(
                            isSelected = state.isSelectedState[index] ?: false,
                            onClick = { viewModel.onImageSelected(
                                imageUri = image.path,
                                newIndex = index
                            )}
                        )
                    }

                }

            }
        }
    }


}

@Composable
fun CircleIconButton(
    isSelected: Boolean,
    onClick: ()->Unit,
    modifier : Modifier = Modifier
){
    val color = if (isSelected) Color.Yellow else Color(0xFFC4F1E8)
    IconButton(
        onClick = { onClick() }
    ) {
        Icon(
            painter = painterResource(id = AppImg.ic_circle),
            contentDescription = null,
            modifier = modifier,
            tint = color
        )
    }
}

