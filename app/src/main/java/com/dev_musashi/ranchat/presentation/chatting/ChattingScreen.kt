package com.dev_musashi.ranchat.presentation.chatting

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev_musashi.ranchat.R
import com.dev_musashi.ranchat.presentation.composable.*
import com.dev_musashi.ranchat.util.BackHandler
import com.dev_musashi.ranchat.util.SocketController.mSocket
import com.dev_musashi.ranchat.util.addFocusCleaner
import kotlinx.coroutines.launch
import com.dev_musashi.ranchat.R.drawable as AppImg
import com.dev_musashi.ranchat.R.string as AppText

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ChattingScreen(
    nickname: String,
    open: (String) -> Unit,
    popUp: () -> Unit,
    viewModel: ChattingViewModel = hiltViewModel()
) {

    val state by viewModel.state
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val focusManager = LocalFocusManager.current
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        if (state.init) {
            viewModel.init(nickname = nickname)
        }
    }

    BackHandler(true) {
        scope.launch {
            if (sheetState.isCollapsed)
                if (state.init) {
                    mSocket.emit("cancelFinding", nickname)
                } else {
                    viewModel.openDialog()
                }
            else {
                sheetState.collapse()
                viewModel.onBackBottomSheetClick()
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        scope.launch {
                            if (sheetState.isCollapsed) sheetState.expand()
                            else sheetState.collapse()
                        }
                        viewModel.onBackBottomSheetClick()
                    }) {
                        Icon(
                            painter = painterResource(id = AppImg.ic_back),
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }

                    IconButton(onClick = {
                        scope.launch { sheetState.collapse() }
                        viewModel.onImageSend()
                    }) {
                        Icon(
                            painter = painterResource(id = AppImg.ic_send2),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                }

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    itemsIndexed(items = state.galleryImages) { index, mediaStoreImage ->
                        GlideImageItem(
                            isSelected = state.isSelectedState[index] ?: false,
                            model = mediaStoreImage,
                            modifier = Modifier.fillParentMaxWidth(0.3f),
                            onClick = {
                                viewModel.onImageSelected(
                                    imageUri = mediaStoreImage.path,
                                    newIndex = index
                                )
                            },
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        scope.launch {
                            sheetState.collapse()
                            viewModel.moveToGallery(open)
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = AppImg.ic_gallery),
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }
            }
        },
        sheetBackgroundColor = Color.White,
        sheetPeekHeight = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(
                    focusManager,
                    bottomSheetState = sheetState,
                    coroutineScope = scope
                )

        ) {
            ChatBar(
                isRefresh = state.refresh,
                onBack = { viewModel.openDialog() },
                onRefresh = { viewModel.refresh() },
                onExit = { viewModel.openDialog() }
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = true
            ) {
                items(items = state.messageList) { item ->
                    val alignment =
                        if (item.nickname != nickname) Alignment.Start else Alignment.End
                    val color =
                        if (item.nickname != nickname) Color(0xFF80CBC4) else Color(0xFFFFF59D)
                    MessageList(
                        item = item,
                        alignment = alignment,
                        color = color,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp, 0.dp, 5.dp, 5.dp),
                        onClick = {
                            scope.launch {
                                sheetState.collapse()
                                viewModel.onImageTouch(it, open)
                            }
                        },
                    )
                }

            }

            CustomTextFieldBar(
                focusManager = focusManager,
                focusRequester = focusRequester,
                text = state.comment,
                enable = state.textField,
                textChanged = { viewModel.onMessageChanged(it) },
                sendClick = { viewModel.onSendClick() },
                galleryClick = {
                    scope.launch {
                        if (sheetState.isCollapsed) {
                            keyboardController?.hide()
                            sheetState.expand()
                            viewModel.openBottomSheetListGallery()
                        } else
                            sheetState.collapse()
                    }

                }
            )
        }

        if (state.openDialog) {
            CustomDialog(
                text = AppText.exitRoom,
                onDismissRequest = { viewModel.closeDialog() },
                confirmButton = { viewModel.back(popUp) },
                dismissButton = { viewModel.closeDialog() }
            )
        }
    }
}






