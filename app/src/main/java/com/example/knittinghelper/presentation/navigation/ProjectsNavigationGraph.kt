package com.example.knittinghelper.presentation.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.projects.screens.*

fun NavGraphBuilder.projectsNavGraph(navController: NavHostController) {

    composable(route = Screens.ProjectsScreen.route) {
        ProjectsScreen(navController)
    }
    composable(route = Screens.CreateProjectScreen.route) {
        CreateProjectScreen(navController)
    }
    composable(
        route = Screens.ProjectScreen.route,
        arguments = listOf(navArgument("projectId") { type = NavType.StringType })
    ) {
        ProjectScreen(navController)
    }
    composable(
        route = Screens.CreatePartScreen.route,
        arguments = listOf(navArgument("projectId") { type = NavType.StringType })) {
        CreatePartScreen(navController)
    }
    composable(
        route = Screens.PartScreen.route,
        arguments = listOf(
            navArgument("projectId") { type = NavType.StringType },
            navArgument("partId") { type = NavType.StringType },
            navArgument("count") { type = NavType.StringType }
        )
    ) {
        PartScreen(navController)
    }
}