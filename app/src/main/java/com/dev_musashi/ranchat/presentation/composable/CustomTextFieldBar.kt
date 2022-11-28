package com.dev_musashi.ranchat.presentation.composable

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev_musashi.ranchat.R

@Composable
fun CustomTextFieldBar(
    focusManager: FocusManager,
    focusRequester: FocusRequester,
    text: String,
    textChanged: (String) -> Unit,
    enable: Boolean,
    sendClick: () -> Unit,
    galleryClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.width(30.dp),
            onClick = { galleryClick() },
            enabled = enable,
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.ic_gallery),
                contentDescription = null,
                tint = Color.White
            )
        }
        TextField(
            enabled = enable,
            shape = CircleShape,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle.Default.copy(color = Color.Black, fontSize = 12.sp),
            value = text,
            onValueChange = { textChanged(it) },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.messagePlaceHolder),
                    fontSize = 13.sp
                )
            }
        )
        IconButton(
            modifier = Modifier.width(30.dp),
            onClick = { sendClick() },
            enabled = enable
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.ic_send2),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}