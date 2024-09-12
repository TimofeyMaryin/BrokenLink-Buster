package com.broken.link.buster.presentation.fragment

import android.util.Log
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.broken.link.buster.MainActivity
import com.broken.link.buster.data._const.TAG
import com.broken.link.buster.presentation.UI_element.FragmentContainer
import com.google.firebase.firestore.auth.User

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FolderFragment(
    activity: MainActivity,
    navController: NavController,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(500),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )
    var getAllFoldersTriggers by remember { mutableIntStateOf(0) }


    var getAllLinksTriggers by remember { mutableIntStateOf(0) }
    var createUserFolderTrigger by remember { mutableIntStateOf(0) }

    var isLoad by remember { mutableStateOf(false) }
    var isShowForm by remember { mutableStateOf(false) }

    var userLink = remember { mutableListOf<String>() }

    LaunchedEffect(key1 = Unit) {
        isLoad = true

        activity.getUserLink(
            onSuccess = {
                userLink = it.toMutableList()
                Log.e(TAG, "FolderFragment: Unit users links are got ${userLink.joinToString(" | ")}", )
                isLoad = false
            },
            onError = {
                Log.e(TAG, "FolderFragment: Unit users links are not got", )
                isLoad = false
            }
        )
    }

    LaunchedEffect(key1 = getAllLinksTriggers) {
        if (getAllLinksTriggers > 0) {
            isLoad = true

            activity.getUserLink(
                onSuccess = {
                    userLink = it.toMutableList()
                    Log.e(TAG, "FolderFragment: links are got ${userLink.joinToString(" | ")}", )
                    isLoad = false
                },
                onError = {
                    Log.e(TAG, "FolderFragment: links are not got", )
                    isLoad = false
                }
            )
        }
    }

    LaunchedEffect(key1 = createUserFolderTrigger) {
        if (createUserFolderTrigger > 0) {
            activity.createUserFolder(
                name = "Folder $createUserFolderTrigger",
                links = listOf(""),
                onSuccess = {},
                onError = {
                    Log.e(TAG, "FolderFragment error msg: $it", )
                }
            )
        }
    }

    FragmentContainer(navController = navController) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.fillMaxSize(.9f), contentAlignment = Alignment.BottomCenter) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp)
                        .background(Color.Blue)
                        .clickable {
                            isShowForm = true

                            // getAllLinksTriggers++
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
                }
            }
        }

        if (isLoad) {
            CircularProgressIndicator(progress = { progress })
        } else {

            LazyColumn {

                stickyHeader {
                    Row(
                        modifier = Modifier.fillParentMaxWidth(.9f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Всего ссылок: ${userLink.size}")

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {

                            IconButton(onClick = { getAllLinksTriggers++}) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                            }

                            IconButton(onClick = { createUserFolderTrigger++}) {
                                Icon(imageVector = Icons.Default.Create, contentDescription = null)
                            }

                        }
                    }
                }

                this.items(userLink) { link ->
                    Text(text = link, modifier = Modifier
                        .fillParentMaxWidth()
                        .background(Color.Red.copy(.2f))
                        .padding(vertical = 20.dp))
                }

            }
        }

    }

    if (isShowForm) {
        Dialog(onDismissRequest = { isShowForm = false }) {

            if (isLoad) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.7f)
                    .background(MaterialTheme.colorScheme.error), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(progress = { progress })
                }
            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.7f)
                        .background(MaterialTheme.colorScheme.primary),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    stickyHeader {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(text = "Выберите ссылки для добавления их в папку")

                            IconButton(onClick = { getAllLinksTriggers++ }) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                            }
                        }
                    }

                    this.items(userLink) { link ->
                        val userLinkFormatter by remember {
                            mutableStateOf(UserLinks(links = link))
                        }
                        SelectedUserLinkItem(
                            value = userLinkFormatter.links,
                            status = userLinkFormatter.enable.value
                        ) {
                            userLinkFormatter.enable.value = !userLinkFormatter.enable.value
                        }
//                        Box(modifier = Modifier.padding(vertical = 10.dp)) {
//                            Box(
//                                modifier = Modifier
//                                    .clip(MaterialTheme.shapes.small)
//                                    .fillParentMaxWidth(.9f)
//                                    .height(40.dp)
//                                    .background(MaterialTheme.colorScheme.inversePrimary),
//                                contentAlignment = Alignment.Center,
//                            ) {
//                                Text(text = link)
//                            }
//
//                        }
                    }
                }
            }
        }
    }

}

data class UserLinks(
    val links: String,
    val enable: MutableState<Boolean> = mutableStateOf(false)
)


@Composable
private fun SelectedUserLinkItem(
    value: String,
    status: Boolean,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), contentAlignment = Alignment.Center) {

        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxWidth(.9f)
                .height(50.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(if (status) Color.Blue else Color.Gray)
                    .size(30.dp),
                contentAlignment = Alignment.Center
            ) {
                if (status) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
                }
            }

            Text(text = value)
        }
    }
}