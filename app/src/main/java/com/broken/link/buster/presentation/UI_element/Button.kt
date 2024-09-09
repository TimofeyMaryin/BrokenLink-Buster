package com.broken.link.buster.presentation.UI_element

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.broken.link.buster.R

@Composable
fun GoogleButton(
    onClick: () -> Unit,
) {
    Button(onClick = { onClick() }) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.icon_google), contentDescription = null, tint = Color.Unspecified)
            Text(text = "Google")
        }
    }
}


