package com.example.knittinghelper.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.profile.screens.*


fun NavGraphBuilder.profileNavGraph(navController: NavHostController) {

    composable(route = Screens.MyProfileScreen.route) {
        MyProfileScreen(navController)
    }
    composable(route = Screens.SettingsScreen.route) {
        SettingsScreen(navController)
    }
    composable(route = Screens.YarnStockScreen.route) {
        YarnStockScreen(navController)
    }
    composable(route = Screens.CreateYarnScreen.route) {
        CreateYarnScreen(navController)
    }
    composable(route = Screens.NeedleStockScreen.route) {
        NeedleStockScreen(navController)
    }
    composable(route = Screens.CreateNeedleScreen.route) {
        CreateNeedleScreen(navController)
    }
    composable(route = Screens.SubsScreen.route) {
        SubsScreen(navController)
    }
    composable(
        route = Screens.SubScreen.route,
        arguments = listOf(
            navArgument("userId") {type = NavType.StringType},
            navArgument("sub") {type = NavType.StringType}
        )) {
        ProfileScreen(navController)
    }
}