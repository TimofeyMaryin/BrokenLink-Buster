package com.broken.link.buster.presentation.navigation

sealed class Screen (val route: String) {
    data object SearchScreen: Screen("search-screen")
    data object FolderScreen : Screen("folder-screen")
    data object SettingsScreen : Screen("settings-screen")
}