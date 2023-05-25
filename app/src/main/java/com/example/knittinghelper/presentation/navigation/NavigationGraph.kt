package com.example.knittinghelper.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.knittinghelper.R
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.auth.SignInScreen
import com.example.knittinghelper.presentation.auth.SignUpScreen

enum class TabDirections(
    val route: String,
    val icon: ImageVector
) {
    SOCIAL("social_root/", Icons.Filled.Email),
    PROJECTS("projects_root/", Icons.Filled.List),
    PROFILE("profile_root/", Icons.Filled.AccountCircle),
}
@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.route
    ) {
        composable(route = Screens.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(route = Screens.SignInScreen.route) {
            SignInScreen(navController)
        }
        composable(route = Screens.SignUpScreen.route) {
            SignUpScreen(navController)
        }

        navigation(
            route = TabDirections.SOCIAL.route,
            startDestination = Screens.FeedScreen.route
        ) {
            socialNavGraph(navController)
        }

        navigation(
            route = TabDirections.PROJECTS.route,
            startDestination = Screens.ProjectsScreen.route
        ) {
            projectsNavGraph(navController)
        }

        navigation(
            route = TabDirections.PROFILE.route,
            startDestination = Screens.MyProfileScreen.route
        ) {
            profileNavGraph(navController)
        }
    }
}