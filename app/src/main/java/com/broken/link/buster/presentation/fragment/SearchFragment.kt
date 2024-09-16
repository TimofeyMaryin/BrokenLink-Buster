package com.broken.link.buster.presentation.fragment

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.broken.link.buster.MainActivity
import com.broken.link.buster.data._const.TAG
import com.broken.link.buster.data._const.isUrlValid
import com.broken.link.buster.presentation.UI_element.FragmentContainer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchFragment(
    activity: MainActivity,
    navController: NavController,
) {
    var userLinks = remember { mutableListOf<String>() }
    var userLink by remember { mutableStateOf("") }
    val alphaChannelForActionButton by animateFloatAsState(targetValue = if (isUrlValid(userLink)) 1f else 0.1f)

    var saveUserLinksTrigger by remember { mutableIntStateOf(0) }
    var showUserLinksTrigger by remember { mutableIntStateOf(0) }
    var removeUserDataBaseTrigger by remember { mutableIntStateOf(0) }
    var createNewDataBaseTrigger by remember { mutableIntStateOf(0) }
    var removeCurrentLinkTrigger by remember { mutableIntStateOf(0) }

    var selectedItem by remember { mutableStateOf<String?>(null) }

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

    LaunchedEffect(key1 = createNewDataBaseTrigger) {
        if (createNewDataBaseTrigger > 0) {
            isLoad = true
            activity.createNewDataBase(
                data = userLink,
                onSuccess = {
                    showUserLinksTrigger++
                    isLoad = false
                }
            )
        }
    }


    LaunchedEffect(key1 = Unit) {
        Log.e(TAG, "SearchFragment: Unit", )
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

    LaunchedEffect(key1 = removeCurrentLinkTrigger)  {
        if (removeCurrentLinkTrigger > 0) {
            Log.e(TAG, "SearchFragment: deleteCurrentLinkTrigger", )

            activity.removeLinkFromFirestore(
                link = selectedItem!!,
                onSuccess = {
                    Log.e(TAG, "SearchFragment deleteCurrentLinkTrigger: success", )
                    showUserLinksTrigger++
                },
                onError = {
                    Log.e(TAG, "SearchFragment deleteCurrentLinkTrigger: error msg = $it", )
                    showUserLinksTrigger++
                }

            )

        }
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

    LaunchedEffect(key1 = removeUserDataBaseTrigger) {
        if (removeUserDataBaseTrigger > 0) {

            Log.e(TAG, "SearchFragment: deleteUserLinkTrigger", )
            activity.removeAllUserLink(
                onSuccess = {
                    showUserLinksTrigger++
                    Log.e(TAG, "SearchFragment: delete user was success", )
                },
                onError = {
                    Log.e("TAG", "SearchFragment deleteUserLinkTrigger: error", )
                }
            )
        }
    }

    FragmentContainer(navController = navController) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.fillMaxSize(.9f), contentAlignment = Alignment.BottomCenter) {
                Text(text = activity.getUserId() ?: "empty")
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(.9f),
        ) {
            TextField(
                value = userLink,
                onValueChange = { userLink = it },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                isError = !isUrlValid(userLink),
                maxLines = 1,
                singleLine = true,
                trailingIcon = {
                    if (isUrlValid(userLink)) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Log.e(TAG, "SearchFragment: ", )
                    }
                }
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = {
                        saveUserLinksTrigger++
                    }
                ) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alphaChannelForActionButton))
                }

                IconButton(onClick = { saveUserLinksTrigger++ }) {
                    Icon(imageVector = Icons.Default.Create, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alphaChannelForActionButton))
                }

                IconButton(onClick = { showUserLinksTrigger++ }) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alphaChannelForActionButton))
                }

                IconButton(onClick = { removeUserDataBaseTrigger++ }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alphaChannelForActionButton))
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
                                    .combinedClickable(
                                        onClick = {
                                            Toast
                                                .makeText(
                                                    activity,
                                                    "Plain Click",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        },
                                        onLongClick = {
                                            selectedItem = link
                                            Toast
                                                .makeText(
                                                    activity,
                                                    "Long Click",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    )
                                    .height(50.dp)
                                    .graphicsLayer {
                                        // Apply alpha based on whether the item is selected
                                        alpha =
                                            if (selectedItem == null || selectedItem == link) 1f else 0.3f
                                    },
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

    selectedItem?.let { selected ->

        Dialog(
            onDismissRequest = { selectedItem = null }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.inversePrimary)
                            .fillMaxWidth(.9f)
                            .height(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = selected, style = MaterialTheme.typography.headlineLarge, color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.inversePrimary)
                        .fillMaxWidth(.9f)
                        .height(70.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Row(
                        modifier = Modifier.fillMaxSize(.9f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                        }

                        IconButton(onClick = { removeCurrentLinkTrigger++ }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }

                        IconButton(onClick = { selectedItem = null; showUserLinksTrigger++ }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null)
                        }

                    }
                }

                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(onClick = { /* Do action 1 */ }) {
                        Text(text = "Delete")
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(onClick = { /* Do action 2 */ }) {
                        Text(text = "Action 2")
                    }

                }

                Button(onClick = { selectedItem = null; showUserLinksTrigger++ }) {
                    Text(text = "Close")
                }
            }

        }
    }
}



