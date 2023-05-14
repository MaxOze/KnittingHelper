package com.example.knittinghelper.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.knittinghelper.presentation.Screens

@Composable
fun BottomNavigationMenu(selectedItem: Int, navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Email, contentDescription = "social") },
            label = { Text("Сообщество") },
            selected = selectedItem == 0,
            onClick = {
                navController.navigate(Screens.FeedScreen.route)}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Email, contentDescription = "projects") },
            label = { Text("Проекты") },
            selected = selectedItem == 1,
            onClick = {
                navController.navigate(Screens.ProjectsScreen.route)}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Email, contentDescription = "profile") },
            label = { Text("Профиль") },
            selected = selectedItem == 0,
            onClick = {
                navController.navigate(Screens.ProfileScreen.route)}
        )
    }
}