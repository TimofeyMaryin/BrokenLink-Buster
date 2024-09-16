package com.broken.link.buster.presentation.fragment

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.broken.link.buster.MainActivity
import com.broken.link.buster.data._const.TAG
import com.broken.link.buster.presentation.UI_element.Container
import com.broken.link.buster.presentation.UI_element.FolderInfoModel
import com.broken.link.buster.presentation.UI_element.FolderItem
import com.broken.link.buster.presentation.UI_element.FragmentContainer
import com.broken.link.buster.presentation.navigation.Screen
import com.broken.link.buster.presentation.vms.UserClientViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FolderFragment(
    activity: MainActivity,
    viewModel: UserClientViewModel,
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

    var nameOfFolder by remember { mutableStateOf("") }
    var folderLink = remember { mutableListOf<String>() }

    var allFolders = remember { mutableListOf<FolderInfoModel>() }

    LaunchedEffect(key1 = Unit) {
        if (getAllLinksTriggers == 0) {
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

    }


    LaunchedEffect(key1 = getAllFoldersTriggers) {
        if (getAllFoldersTriggers > 0) {
            isLoad = true

            activity.getUserFolder(
                onSuccess = { folders ->
                    allFolders = mutableListOf()
                    Log.e(TAG, "FolderFragment get all folders: ${folders.joinToString(" || ")}", )

                    Log.e(TAG, "-".repeat(50), )
                    for ( folder in folders) {
                        val key = folder.keys
                        val folderModel = FolderInfoModel(
                            name = key.joinToString(),
                            links = folder[key.joinToString()]?.toMutableList() ?: mutableListOf()
                        )
                        allFolders.add(folderModel)
                        Log.e(TAG, "FolderFragment folder model: $folderModel", )
                    }
                    Log.e(TAG, "-".repeat(50), )
                    isLoad = false
               },
                onError = {
                    Log.e(TAG, "FolderFragment cannot get folder: $it", )
                    isLoad = false
                }
            )
        }
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
                name = nameOfFolder,
                links = folderLink,
                onSuccess = {
                    Log.e(TAG, "FolderFragment: folder was create", )
                },
                onError = {
                    Log.e(TAG, "FolderFragment error msg: $it", )
                }
            )
        }
    }

    FragmentContainer(navController = navController) {

        ConstraintLayout {
            val (content_ref, fab_ref) = createRefs()


            Column(
                modifier = Modifier
                    .fillMaxSize(.9f)
                    .constrainAs(content_ref) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
            ) {
                Container(weight = 1f, alignment = Alignment.CenterStart) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Папки",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        IconButton(onClick = { getAllFoldersTriggers++ }) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                        }

                    }
                }

                Container(weight = 10f) {
                    if (isLoad) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(progress = { progress })
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            this.items(items = allFolders) { folder ->
                                FolderItem(name = folder.name, links = folder.links) {

                                    viewModel.setCurrentFolderValue(folder)
                                    navController.navigate(Screen.ShowCurrentFolderScreen.route)

                                    Toast.makeText(activity, "YAP!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }
                }
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp)
                    .background(Color.Blue)
                    .constrainAs(fab_ref) {
                        bottom.linkTo(parent.bottom, )
                        end.linkTo(parent.end, margin = 20.dp)
                    }
                    .clickable { isShowForm = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
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
                            Text(
                                text = "Выберите ссылки для добавления их в папку",
                                textAlign = TextAlign.Center,
                            )

                            IconButton(onClick = { getAllLinksTriggers++ }) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                            }
                        }
                    }

                    item {
                        TextField(
                            value = nameOfFolder,
                            onValueChange = { nameOfFolder = it },
                            placeholder = { Text(text = "Название папки") }
                        )
                    }

                    this.items(userLink) { link ->
                        val userLinkFormatter by remember {
                            mutableStateOf(UserLinks(links = link))
                        }
                        SelectedUserLinkItem(
                            value = userLinkFormatter.links,
                            status = userLinkFormatter.enable.value
                        ) {
                            folderLink.add(link)
                            userLinkFormatter.enable.value = !userLinkFormatter.enable.value
                        }
                    }

                    item {
                        Button(
                            onClick = { createUserFolderTrigger++ },
                            enabled = nameOfFolder.isNotEmpty() && folderLink.isNotEmpty()
                        ) {
                            Text(text = "Создать папку")
                        }
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
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp), contentAlignment = Alignment.Center) {

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