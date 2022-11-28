package com.dev_musashi.ranchat.presentation.composable

import androidx.annotation.DrawableRes
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun CustomIconButton(
    onClick: ()->Unit,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    tint: Color
) {
    IconButton(
        onClick = { onClick() }
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = modifier,
            tint = tint
        )
    }
}