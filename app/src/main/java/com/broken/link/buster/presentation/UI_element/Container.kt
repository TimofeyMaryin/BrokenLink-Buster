package com.broken.link.buster.presentation.UI_element

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ColumnScope.Container(
    weight: Float,
    alignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .weight(weight),
        contentAlignment = alignment
    ) {
        content()
    }
}


@Composable
fun RowScope.Container(
    weight: Float,
    alignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .weight(weight),
        contentAlignment = alignment
    ) {
        content()
    }
}