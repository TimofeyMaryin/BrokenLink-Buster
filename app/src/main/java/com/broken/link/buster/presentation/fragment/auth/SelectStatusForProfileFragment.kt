package com.broken.link.buster.presentation.fragment.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.broken.link.buster.presentation.navigation.AuthentificationGuestScreen

@Composable
fun SelectStatusForProfileFragment(
    navController: NavController,
) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {
            navController.navigate(AuthentificationGuestScreen.FinishScreen.route)
        }) {
            Text(text = "Продолжить (2/3)")
        }
    }

}