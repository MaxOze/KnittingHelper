package com.example.knittinghelper.presentation.projects

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePartScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Создание части") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screens.ProfileScreen.route) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "backToProfile"
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        },
        bottomBar = {
            BottomNavigationMenu(navController = navController)
        }
    ) {

    }
}