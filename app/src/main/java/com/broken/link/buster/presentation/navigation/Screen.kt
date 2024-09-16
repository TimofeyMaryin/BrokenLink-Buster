package com.broken.link.buster.presentation.navigation

sealed class Screen (val route: String) {
    data object SearchScreen: Screen("search-screen")
    data object FolderScreen : Screen("folder-screen")
    data object SettingsScreen : Screen("settings-screen")
    data object ShowCurrentFolderScreen : Screen("show-current-folder-screen")
    data object ShowWebSiteCodeScreen : Screen("show-web-site-code-screen")
}


sealed class AuthentificationGuestScreen (val route: String) {
    data object FillProfileInfoScreen : Screen("fill-profile-info-screen")
    data object SelectStatusForProfileScreen : Screen("selecte-status-for-profile-screen")
    data object FinishScreen : Screen("finish-screen")
}