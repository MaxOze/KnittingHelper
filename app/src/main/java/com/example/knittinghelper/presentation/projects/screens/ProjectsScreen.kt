package com.example.knittinghelper.presentation.projects.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.components.cards.ProjectCardComponent
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu
import com.example.knittinghelper.presentation.projects.viewmodels.ProjectsViewModel
import com.example.knittinghelper.util.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()
    val expandedFab = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val delete =  remember { mutableStateOf("") }
    val deleteSuccess =  remember { mutableStateOf(0) }

    val viewModel: ProjectsViewModel = hiltViewModel()

    LaunchedEffect(deleteSuccess) {
        viewModel.getUserProjects()
    }
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screens.CreateProjectScreen.route) },
                expanded = expandedFab.value,
                icon = { Icon(Icons.Filled.Add, "add icon") },
                text = { Text(text = "Создать новый проект") },
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomNavigationMenu(navController = navController)
        }
    ) {
        when (val response = viewModel.getUserProjectsData.value) {
            is Response.Loading -> {
                Snackbar(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(text = "Загрузка проектов...")
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            is Response.Success -> {
                if (delete.value.isNotEmpty()) {
                    AlertDialog(
                        onDismissRequest = {
                            delete.value = ""
                        }
                    ) {
                        Surface(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight(),
                            shape = MaterialTheme.shapes.large,
                            tonalElevation = AlertDialogDefaults.TonalElevation
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                    Text(
                                        text = "Удалить этот проект без возможности возврата?",
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        TextButton(
                                            onClick = {
                                                viewModel.deleteProject(delete.value)
                                                delete.value = ""
                                            },
                                        ) {
                                            Text("Да")
                                        }
                                        TextButton(
                                            onClick = {
                                                delete.value = ""
                                            },
                                        ) {
                                        Text("Нет")
                                        }
                                    }
                            }
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.padding(top = it.calculateTopPadding()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (response.data.isEmpty()) {
                        item {
                            Text(text = "Добавьте новый проект!", color = Color.Blue)
                        }
                    } else {
                        val projects = response.data
                        items(projects) { project ->
                            ProjectCardComponent(delete, project, navController)
                        }
                    }
                }
                when (val deleteResponse = viewModel.deleteProjectData.value) {
                    is Response.Loading -> {
                        AlertDialog(onDismissRequest = { }) {
                            Surface(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .wrapContentHeight(),
                                shape = MaterialTheme.shapes.large,
                                tonalElevation = AlertDialogDefaults.TonalElevation
                            ) {
                                Text(text = "Удаление проекта...")
                            }
                        }
                    }
                    is Response.Success -> {
                        if (deleteResponse.data) {
                            deleteSuccess.value++
                            AlertDialog(onDismissRequest = { }) {
                                Surface(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .wrapContentHeight(),
                                    shape = MaterialTheme.shapes.large,
                                    tonalElevation = AlertDialogDefaults.TonalElevation
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "Проект удален",
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))
                                        TextButton(
                                            onClick = { viewModel.deleteOk() },
                                            modifier = Modifier.align(Alignment.End)
                                        ) {
                                            Text("Ок")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    is Response.Error -> {
                        AlertDialog(onDismissRequest = { }) {
                            Surface(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .wrapContentHeight(),
                                shape = MaterialTheme.shapes.large,
                                tonalElevation = AlertDialogDefaults.TonalElevation
                            ) {
                                Text(text = deleteResponse.message)
                            }
                        }
                    }
                }
            }
            is Response.Error -> {
                Snackbar(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = response.message)
                    }
                }
            }
        }
    }
}