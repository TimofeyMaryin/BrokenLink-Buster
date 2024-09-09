package com.broken.link.buster.presentation.fragment.auth

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.broken.link.buster.presentation.navigation.AuthentificationGuestScreen

@Composable
fun FillProfileInfoFragment(
    activity: ComponentActivity,
    navController: NavController,
) {
    var name by remember { mutableStateOf("") }
    var about by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(.9f),
            leadingIcon = { Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null,) },
            placeholder = { Text(text = "Введите имя") }
        )
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = about,
            onValueChange = { about = it },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(.9f),
            leadingIcon = { Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null,) },
            placeholder = { Text(text = "Введите о себе") }
        )
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.fillMaxSize(.9f), contentAlignment = Alignment.BottomCenter) {
            Button(
                onClick = {
                    activity.intent.apply {
                        putExtra("name", name)
                        putExtra("about", about)
                    }

                    Log.e("TAG", "FillProfileInfoFragment: name = ${activity.intent.getStringExtra("name")}; about = ${activity.intent.getStringExtra("about")}", )

                    navController.navigate(AuthentificationGuestScreen.SelectStatusForProfileScreen.route)
                },
                enabled = name.isNotEmpty() && about.isNotEmpty(),
            ) {
                Text(text = "Продолжить (1/3)")
            }
        }
    }

}