package com.broken.link.buster.presentation.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.broken.link.buster.presentation.fragment.auth.FillProfileInfoFragment
import com.broken.link.buster.presentation.fragment.auth.FinishFragment
import com.broken.link.buster.presentation.fragment.auth.SelectStatusForProfileFragment

@Composable
fun AuthentificationGuestNavigation(activity: ComponentActivity) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthentificationGuestScreen.FillProfileInfoScreen.route
    ) {
        this.composable(
            route = AuthentificationGuestScreen.FillProfileInfoScreen.route
        ) {
            FillProfileInfoFragment(navController = navController, activity = activity)
        }

        this.composable(
            route = AuthentificationGuestScreen.SelectStatusForProfileScreen.route,
        ) {
            SelectStatusForProfileFragment(navController = navController)
        }

        this.composable(
            route = AuthentificationGuestScreen.FinishScreen.route
        ) {
            FinishFragment(activity = activity)
        }
    }
}