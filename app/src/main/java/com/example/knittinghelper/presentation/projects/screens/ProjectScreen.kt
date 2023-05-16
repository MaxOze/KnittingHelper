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
import com.example.knittinghelper.presentation.components.PartCardComponent
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu
import com.example.knittinghelper.presentation.projects.viewmodels.ProjectViewModel
import com.example.knittinghelper.presentation.projects.viewmodels.ProjectsViewModel
import com.example.knittinghelper.util.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val viewModel: ProjectViewModel = hiltViewModel()
    viewModel.getProjectInfo()
    viewModel.getProjectParts()
    when(val projectResponse = viewModel.getProjectData.value) {
        is Response.Loading -> {
            CircularProgressIndicator()
        }
        is Response.Success -> {
            if(projectResponse.data != null) {
                when(val partsResponse = viewModel.getProjectPartsData.value) {
                    is Response.Loading -> {
                        CircularProgressIndicator()
                    }
                    is Response.Success -> {
                        val project = projectResponse.data
                        val parts = partsResponse.data
                        Scaffold(
                            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                            topBar = {
                                TopAppBar(
                                    scrollBehavior = scrollBehavior,
                                    title = { Text("Проект " + project.name) },
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
                                            navController.navigate("projects-create/" + project.projectId + "/")
                                        }, modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Text(text = "Создать новую часть")
                                    }
                                }
                                if (parts.isEmpty()) {
                                    item {
                                        Text(text = "Добавьте новую часть проекта!", color = Color.Blue)
                                    }
                                } else {
                                    items(parts) { part ->
                                        PartCardComponent(part, navController)
                                    }
                                }
                            }
                        }
                    }
                    is Response.Error -> {
                        Toast.makeText(LocalContext.current, partsResponse.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        is Response.Error -> {
            Toast.makeText(LocalContext.current, projectResponse.message, Toast.LENGTH_SHORT).show()
        }
    }

}