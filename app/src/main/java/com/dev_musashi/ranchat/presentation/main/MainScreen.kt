package com.dev_musashi.ranchat.presentation.main

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev_musashi.ranchat.presentation.main.MainViewModel
import com.dev_musashi.ranchat.util.SocketController
import com.dev_musashi.ranchat.R.drawable as AppImg
import com.dev_musashi.ranchat.util.addFocusCleaner

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    open: (String) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager, bottomSheetState = null, coroutineScope = null),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Icon(
            painter = painterResource(id = AppImg.ic_send),
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(text = "RandomChat", style = MaterialTheme.typography.h4, color = Color.White)
        Spacer(modifier = Modifier.height(25.dp))
        Text(text = "닉네임", style = MaterialTheme.typography.h5, color = Color.White)
        Spacer(modifier = Modifier.height(25.dp))
        TextField(
            enabled = state.textField,
            value = state.nickName,
            onValueChange = { viewModel.onValueChanged(it) },
            shape = CircleShape,
            modifier = Modifier
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
            textStyle = TextStyle.Default.copy(color = Color.Black),
            placeholder = {
                Text(text = "닉네임을 입력해주세요.")
            },
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(25.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            Button(
                shape = CircleShape,
                modifier = Modifier.width(100.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1966D2)),
                enabled = state.nicknameCheck,
                onClick = {
                    focusManager.clearFocus()
                    viewModel.nicknameCheck()
                },
            ) {
                val color = if(state.nicknameCheck) Color.White else Color.Black
                Text(text = "확인", color = color)
            }
            Button(
                modifier = Modifier.width(100.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1966D2)),
                enabled = state.chattingOkay,
                onClick = {
                    viewModel.open(open)
                }
            ) {
                val color = if(state.chattingOkay) Color.White else Color.Black
                Text(text = "채팅 시작", color = color)
            }
        }
        Button(onClick = { SocketController.mSocket.emit("check") }) {
            Text(text = "체크")
        }
    }
}