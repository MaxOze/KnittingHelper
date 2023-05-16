package com.example.knittinghelper.presentation.projects

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.components.ProjectCardComponent
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu
import com.example.knittinghelper.presentation.projects.viewmodels.ProjectsViewModel
import com.example.knittinghelper.util.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val viewModel: ProjectsViewModel = hiltViewModel()
    viewModel.getUserProjects()
    when(val response = viewModel.getUserProjectsData.value) {
        is Response.Loading -> {
            CircularProgressIndicator()
        }
        is Response.Success -> {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopAppBar(
                        scrollBehavior = scrollBehavior,
                        title = { Text("Проекты") },
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                        )
                    )
                },
                bottomBar = {
                    BottomNavigationMenu(navController = navController)
                }
            ) {
                LazyColumn(
                    modifier = Modifier.padding(top = it.calculateTopPadding()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Button(
                            onClick = {
                                navController.navigate(Screens.CreateProjectScreen.route)
                            }, modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(text = "Создать новый проект")
                        }
                    }
                    if (response.data.isEmpty()) {
                        item {
                            Text(text = "Добавьте новый проект!", color = Color.Blue)
                        }
                    } else {
                        val projects = response.data
                        items(projects) { project ->
                            ProjectCardComponent(project, navController)
                        }
                    }
                }
            }
        }
        is Response.Error -> {
            Toast.makeText(LocalContext.current, response.message, Toast.LENGTH_SHORT).show()
        }
    }
    
}