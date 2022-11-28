package com.dev_musashi.ranchat.presentation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dev_musashi.ranchat.R

@Composable
fun ChatBar(
    modifier: Modifier = Modifier,
    isRefresh: Boolean,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onExit: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = Modifier,
            onClick = { onBack() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                tint = Color.White
            )
        }
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconButton(
                onClick = { onRefresh() },
                enabled = isRefresh
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_refresh),
                    contentDescription = null,
                    tint = Color.White
                )
            }
            IconButton(
                onClick = { onExit() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_exit),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

    }
    Divider(color = Color(0xFF4F5058))
}