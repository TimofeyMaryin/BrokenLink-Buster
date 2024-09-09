package com.broken.link.buster.presentation.fragment

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.broken.link.buster.MainActivity
import com.broken.link.buster.presentation.UI_element.FragmentContainer

@Composable
fun SearchFragment(
    activity: ComponentActivity,
    navController: NavController,
) {
    FragmentContainer(navController = navController) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Здесь пока ничего нет! SearchFragment")
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {

            }) {
                Text(text = "Add New Offer")
            }
        }


    }
}