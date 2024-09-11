package com.broken.link.buster.presentation.fragment

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.broken.link.buster.MainActivity
import com.broken.link.buster.data.BrokenLinkModel
import com.broken.link.buster.data._const.TAG
import com.broken.link.buster.presentation.UI_element.FragmentContainer

@Composable
fun SearchFragment(
    activity: MainActivity,
    navController: NavController,
) {

    var loadDataCounter by remember { mutableIntStateOf(-1) }

    var loaderLinks = remember { mutableListOf<String>() }
    var loadLinksTrigger by remember { mutableIntStateOf(0) }
    
    var countModelSize by remember { mutableIntStateOf(0) }
    var errorText by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(key1 = loadDataCounter) {
        if (loadDataCounter != -1) {
            activity.loadOfferForUser(
                onSuccess = {
                    countModelSize = it.size
                },
                onError = {
                    errorText = it.message
                }
            )
        }
    }
    
    LaunchedEffect(key1 = loadLinksTrigger) {
        Log.e(TAG, "SearchFragment: LaunchedEffect", )
        activity.getUserLink(
            onSuccess = {
                Log.e(TAG, "SearchFragment: LaunchedEffect success", )
                loaderLinks = it.toMutableList()
            },
            onError = {
                Log.e(TAG, "SearchFragment: LaunchedEffect error", )
            }
        )
    }

    FragmentContainer(navController = navController) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Здесь пока ничего нет! SearchFragment")
            Spacer(modifier = Modifier.height(20.dp))


            var url by remember { mutableStateOf("") }
            TextField(
                value = url,
                onValueChange = { url = it }
            )

            Button(onClick = {
                Log.e("TAG", "SearchFragment: save data", )
                activity.saveLink(
                    url = url,
                    onSuccess = {
                        Toast.makeText(activity, "Link save: $url", Toast.LENGTH_SHORT).show()
                        url = ""
                    },
                    onError = {
                        url = ""
                    }
                )
                loadDataCounter++
                loadLinksTrigger++
            }) {
                Text(text = "Add New Offer")
            }
            
            Button(onClick = { 
                activity.getUserLink(
                    onSuccess = {
                        Toast.makeText(activity, "Get user link", Toast.LENGTH_SHORT).show()
                        loaderLinks = it.toMutableList()
                    },
                    onError = {
                        Toast.makeText(activity, "Cannot get user link", Toast.LENGTH_SHORT).show()
                    }
                )
            }) {
                Text(text = "Get YourLink")
            }


            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Count save models: $countModelSize")
            Text(text = "Error msg: $errorText")
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(text = "Links: ${loaderLinks.joinToString(" , ")}")

        }


    }
}