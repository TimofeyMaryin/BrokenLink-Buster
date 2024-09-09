package com.broken.link.buster.presentation.fragment

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.broken.link.buster.presentation.UI_element.FragmentContainer

@Composable
fun SettingsFragment(
    navController: NavController,
) {
    FragmentContainer(navController = navController) {
        Text(text = "Здесь пока ничего нет! SettingsFragment")
    }
}