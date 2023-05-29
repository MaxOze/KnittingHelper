package com.example.knittinghelper.presentation.navigation


import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

enum class TabDirections(
    val route: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val text: String
) {
    SOCIAL("social_graph", Icons.Outlined.Home, Icons.Filled.Home,"Главная"),
    PROJECTS("projects_graph", Icons.Outlined.LibraryBooks, Icons.Filled.LibraryBooks, "Проекты"),
    PROFILE("profile_graph", Icons.Outlined.PersonOutline, Icons.Filled.Person, "Профиль"),
}

@Composable
fun BottomNavigationMenu(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination
    val tabs = TabDirections.values().toList()
    NavigationBar {
        tabs.forEach { tab ->
            val selected = currentRoute?.hierarchy?.any { it.route == tab.route }
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected == true) tab.selectedIcon else tab.icon,
                        contentDescription = "nav_icon",
                        modifier = Modifier.size(30.dp)
                    )
                },
                label = { Text(tab.text) },
                selected = selected == true,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
