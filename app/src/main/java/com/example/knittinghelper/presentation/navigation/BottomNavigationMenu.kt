package com.example.knittinghelper.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.knittinghelper.presentation.Screens

@Composable
fun BottomNavigationMenu(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: TabDirections.PROJECTS
    val tabs = TabDirections.values().toList()
    NavigationBar {
        tabs.forEach { tab ->
            val route = tab.route

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = "nav_icon",
                    )
                },
                label = { Text(route) },
                selected = currentRoute == route,
                onClick = {
                    if(currentRoute.toString().startsWith(route)) {
                        navController.navigate(findTabRootRoute(route)) {
                            popUpTo(findStartDestination(navController.graph).id)
                        }
                    } else if(route != currentRoute) {
                        navController.navigate(route) {
                            launchSingleTop = true
                            restoreState = true
                            val startDestination = findStartDestination(navController.graph)
                            popUpTo(startDestination.id) {
                                saveState = true
                            }
                        }
                    }

                }
            )
        }
    }
}

tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

fun findTabRootRoute(tab: String): String {
    return when (tab) {
        TabDirections.SOCIAL.route -> Screens.FeedScreen.route
        TabDirections.PROJECTS.route -> Screens.ProjectsScreen.route
        else -> Screens.ProfileScreen.route
    }
}
