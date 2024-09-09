package com.broken.link.buster.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.broken.link.buster.presentation.fragment.FolderFragment
import com.broken.link.buster.presentation.fragment.SearchFragment
import com.broken.link.buster.presentation.fragment.SettingsFragment

@Composable
fun ApplicationNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.SearchScreen.route) {
        this.composable(
            route = Screen.SearchScreen.route
        ) {
            SearchFragment(navController = navController)
        }

        this.composable(
            route = Screen.FolderScreen.route,
        ) {
            FolderFragment(navController = navController)
        }

        this.composable(
            route = Screen.SettingsScreen.route
        ) {
            SettingsFragment(navController = navController)
        }
    }
}