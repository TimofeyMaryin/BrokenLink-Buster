package com.broken.link.buster.presentation.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.broken.link.buster.MainActivity
import com.broken.link.buster.presentation.fragment.CurrentFolderScreen
import com.broken.link.buster.presentation.fragment.FolderFragment
import com.broken.link.buster.presentation.fragment.SearchFragment
import com.broken.link.buster.presentation.fragment.SettingsFragment
import com.broken.link.buster.presentation.fragment.ShowWebSiteCodeFragment
import com.broken.link.buster.presentation.vms.UserClientViewModel

@Composable
fun ApplicationNavigation(activity: MainActivity) {
    val navController = rememberNavController()

    val mainViewModel = viewModel<UserClientViewModel>()

    NavHost(navController = navController, startDestination = Screen.SearchScreen.route) {
        this.composable(
            route = Screen.SearchScreen.route
        ) {
            SearchFragment(navController = navController, activity = activity)
        }

        this.composable(
            route = Screen.FolderScreen.route,
        ) {
            FolderFragment(navController = navController, activity = activity, viewModel = mainViewModel)
        }

        this.composable(
            route = Screen.SettingsScreen.route
        ) {
            SettingsFragment(navController = navController, activity = activity)
        }

        this.composable(
            route = Screen.ShowCurrentFolderScreen.route
        ) {
            CurrentFolderScreen(navController = navController, viewModel = mainViewModel)
        }

        this.composable(
            route = Screen.ShowWebSiteCodeScreen.route
        ) {
            ShowWebSiteCodeFragment(navController = navController, link = mainViewModel.currentUserUrl)
        }
    }
}