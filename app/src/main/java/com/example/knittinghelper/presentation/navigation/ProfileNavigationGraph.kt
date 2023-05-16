package com.example.knittinghelper.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.profile.*


fun NavGraphBuilder.profileNavGraph(navController: NavHostController) {

    composable(route = Screens.ProfileScreen.route) {
        ProfileScreen(navController) {
            navController.navigate(Screens.SettingsScreen.route)
        }
    }
    composable(route = Screens.SettingsScreen.route) {
        SettingsScreen(navController)
    }
}