package com.dev_musashi.ranchat.util

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
fun Modifier.addFocusCleaner(
    focusManager: FocusManager,
    doOnClear: () -> Unit = {},
    bottomSheetState: BottomSheetState?,
    coroutineScope: CoroutineScope?
): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(
            onTap = {
                doOnClear()
                focusManager.clearFocus()
                coroutineScope?.launch {
                    bottomSheetState?.collapse()
                }
            },
            onPress = {
                doOnClear()
                focusManager.clearFocus()
            }
        )
    }
}