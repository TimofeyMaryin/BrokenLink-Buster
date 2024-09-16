package com.broken.link.buster.presentation.fragment

import android.util.Log
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.broken.link.buster.data._const.TAG
import com.broken.link.buster.data.internet.connectInternetConnection
import com.broken.link.buster.presentation.navigation.Screen
import com.broken.link.buster.presentation.vms.UserClientViewModel
import kotlinx.coroutines.delay

@Composable
fun CurrentFolderScreen(
    navController: NavController,
    viewModel: UserClientViewModel,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onBackground),
        contentAlignment = Alignment.Center,
    ) {

        Column(
            modifier = Modifier.fillMaxSize(.9f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = viewModel.currentFolder!!.name,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(100.dp))


            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(viewModel.currentFolder!!.links) {
                    LinksItem(value = it) {
                        viewModel.currentUserUrl = it
                        navController.navigate(Screen.ShowWebSiteCodeScreen.route)
                    }
                }
            }

        }


        Box(modifier = Modifier.fillMaxSize(.9f), contentAlignment = Alignment.TopEnd) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null)
            }
        }
    }

}

@Composable
private fun LinksItem(
    value: String,
    onCheckCode: (String) -> Unit
) {

    var isCheckUrl by remember { mutableStateOf<Boolean?>(null) }
    var checkUrlTrigger by remember { mutableStateOf(0) }

    val infiniteTransition = rememberInfiniteTransition()
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(600),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )


    LaunchedEffect(key1 = checkUrlTrigger) {
        isCheckUrl = null
        delay(40)
        isCheckUrl = connectInternetConnection(value)
        Log.e(TAG, "LinksItem checkUrlTrigger: $isCheckUrl", )
    }


    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .fillMaxWidth()
            .height(180.dp)
            .background(
                if (isCheckUrl == false) {
                    MaterialTheme.colorScheme.error.copy(.3f)
                } else {
                    Color.Transparent
                }
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(.9f)
        ) {
            Row(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.surface
                )


                if (isCheckUrl != null) {
                    Icon(
                        imageVector = if (isCheckUrl == true) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (isCheckUrl == true) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.error,
                    )
                } else {
                    CircularProgressIndicator(progress = { progress })
                }



            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                LinkItemStatusButton(value = "Проверить", ic = Icons.Default.Refresh) {
                    checkUrlTrigger++
                }

                LinkItemStatusButton(value = "Посмотреть код", ic = Icons.Default.Edit) {
                    onCheckCode(value)
                }
            }
        }
    }

}

@Composable
private fun RowScope.LinkItemStatusButton(
    value: String,
    ic: ImageVector,
    onClick: () -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .weight(1f)
        .clickable { onClick() }, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ic,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

    }
}