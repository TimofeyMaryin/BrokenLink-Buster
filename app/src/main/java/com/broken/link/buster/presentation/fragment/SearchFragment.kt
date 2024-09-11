package com.broken.link.buster.presentation.fragment

import android.util.Log
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.broken.link.buster.MainActivity
import com.broken.link.buster.data._const.TAG
import com.broken.link.buster.presentation.UI_element.FragmentContainer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchFragment(
    activity: MainActivity,
    navController: NavController,
) {
    var userLinks = remember { mutableListOf<String>() }
    var userLink by remember { mutableStateOf("") }

    var saveUserLinksTrigger by remember { mutableIntStateOf(0) }
    var showUserLinksTrigger by remember { mutableIntStateOf(0) }
    var deleteUserLinkTrigger by remember { mutableIntStateOf(0) }

    var isLoad by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition()
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(500),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )


    LaunchedEffect(key1 = Unit) {
        Log.e(TAG, "SearchFragment: Unit", )
        // showUserLinksTrigger++
        isLoad = true
        activity.getUserLink(
            onSuccess = {
                userLinks = it.toMutableList()
                Log.e(TAG, "SearchFragment Unit success: ${userLinks.size}", )
                isLoad = false
            },
            onError = {
                Log.e("TAG", "SearchFragment showUserLinksTrigger: error", )
                isLoad = false
            }
        )
    }



    LaunchedEffect(key1 = saveUserLinksTrigger) {
        if (saveUserLinksTrigger > 0) {

            Log.e(TAG, "SearchFragment: saveUserLinksTrigger", )
            isLoad = true
            activity.saveLink(
                url = userLink,
                onSuccess = {
                    showUserLinksTrigger++
                    isLoad = false
                },
                onError = {
                    Log.e("TAG", "SearchFragment saveUserLinksTrigger: error", )
                    isLoad = false
                }
            )
        }
    }

    LaunchedEffect(key1 = showUserLinksTrigger) {
        if (showUserLinksTrigger > 0) {
            Log.e(TAG, "SearchFragment: showUserLinksTrigger", )
            isLoad = true
            activity.getUserLink(
                onSuccess = {
                    userLinks = it.toMutableList()
                    userLink = ""
                    isLoad = false
                },
                onError = {
                    Log.e("TAG", "SearchFragment showUserLinksTrigger: error", )
                    isLoad = false
                }
            )
        }
    }

    LaunchedEffect(key1 = deleteUserLinkTrigger) {
        if (deleteUserLinkTrigger > 0) {

            Log.e(TAG, "SearchFragment: deleteUserLinkTrigger", )
            activity.removeAllUserLink(
                onSuccess = {
                    showUserLinksTrigger++
                },
                onError = {
                    Log.e("TAG", "SearchFragment deleteUserLinkTrigger: error", )
                }
            )
        }
    }

    FragmentContainer(navController = navController) {

        Column(
            modifier = Modifier.fillMaxSize(.9f),
        ) {
            TextField(
                value = userLink,
                onValueChange = { userLink = it },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { saveUserLinksTrigger++ }) {
                    Icon(imageVector = Icons.Default.Create, contentDescription = null)
                }

                IconButton(onClick = { showUserLinksTrigger++ }) {
                    Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
                }

                IconButton(onClick = { deleteUserLinkTrigger++ }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }

            }

            if (!isLoad) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    stickyHeader {
                        Text(text = if (userLinks.isEmpty()) "Cписок пуст" else "Количество элементов ${userLinks.size}")
                    }

                    this.items(userLinks) { link ->
                        Box(modifier = Modifier.padding(vertical = 10.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(Color.Magenta)
                                    .fillParentMaxWidth()
                                    .height(50.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = link,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(progress = { progress })
                }
            }

        }

    }
}