package com.example.knittinghelper.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.social.FeedScreen

fun NavGraphBuilder.socialNavGraph(navController: NavHostController) {

    composable(route = Screens.FeedScreen.route) {
            FeedScreen(navController)
    }

}