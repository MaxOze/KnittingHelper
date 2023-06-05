package com.example.knittinghelper.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.social.screens.*

fun NavGraphBuilder.socialNavGraph(navController: NavHostController) {

    composable(route = Screens.FeedScreen.route) {
            FeedScreen(navController)
    }
    composable(
        route = Screens.ProfileScreen.route,
        arguments = listOf(
            navArgument("userId") {type = NavType.StringType},
            navArgument("sub") {type = NavType.StringType}
            )) {
        ProfileScreen(navController)
    }
}