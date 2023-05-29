package com.example.knittinghelper.presentation.projects.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.knittinghelper.domain.model.Part
import com.example.knittinghelper.domain.model.Project
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.components.cards.PartCardComponent
import com.example.knittinghelper.presentation.components.cards.ProjectCardComponent
import com.example.knittinghelper.presentation.components.cards.SimpleProjectCardComponent
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu
import com.example.knittinghelper.presentation.projects.viewmodels.ProjectViewModel
import com.example.knittinghelper.util.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()
    val expandedFab = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }
    val projectState = remember { mutableStateOf<Project?>(null) }
    val name = remember { mutableStateOf("") }
    val simple = remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    val delete = remember { mutableStateOf<Part?>(null) }
    val deleteSuccess = remember { mutableStateOf(0) }

    val viewModel: ProjectViewModel = hiltViewModel()

    LaunchedEffect(deleteSuccess) {
        viewModel.getProjectInfo()
        viewModel.getProjectParts()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = { Text(name.value) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "backToProjects"
                        )
                    } },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
                 },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (!simple.value) {
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate("projects-create/${projectState.value?.projectId}/") },
                    expanded = expandedFab.value,
                    icon = { Icon(Icons.Filled.Add, "add icon") },
                    text = { Text(text = "Создать новую часть") },
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomNavigationMenu(navController = navController)
        }
    ) {
        when (val response = viewModel.getProjectPartsData.value) {
            is Response.Loading -> {
                Snackbar(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(text = "Загрузка данных проекта...")
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            is Response.Success -> {
                if (delete.value != null) {
                    AlertDialog(
                        onDismissRequest = {
                            delete.value = null
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
                                    text = "Удалить эту часть проекта без возможности возврата?",
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    TextButton(
                                        onClick = {
                                            viewModel.deletePart(delete.value!!, projectState.value!!)
                                            delete.value = null
                                        },
                                    ) {
                                        Text("Да")
                                    }
                                    TextButton(
                                        onClick = {
                                            delete.value = null
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val project = viewModel.getProjectData.value
                    if (project is Response.Success) {
                        if (project.data != null) {
                            name.value = project.data.name
                            simple.value = project.data.simpleProject
                            projectState.value = project.data
                            if (simple.value) {
                                item {
                                    SimpleProjectCardComponent(it, project.data)
                                }
                            } else {
                                item {
                                    ProjectCardComponent(it, project.data)
                                }
                                if (response.data.isEmpty()) {
                                    item {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(top = 50.dp)
                                        ) {
                                            TextButton(onClick = {
                                                navController.navigate("projects-create/${projectState.value?.projectId}/")
                                            }) {
                                                Text(
                                                    text = "Добавьте новые части!",
                                                    color = Color.Blue,
                                                    style = MaterialTheme.typography.titleLarge
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    val parts = response.data
                                    items(parts) { part ->
                                        PartCardComponent(
                                            delete,
                                            project.data.countRows,
                                            part,
                                            navController
                                        )
                                    }
                                    if (parts.size > 1) {
                                        item {
                                            Divider(
                                                thickness = 0.dp,
                                                modifier = Modifier.padding(top = 170.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                when (val deleteResponse = viewModel.deletePartData.value) {
                    is Response.Loading -> {
                        AlertDialog(onDismissRequest = { }) {
                            Surface(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .wrapContentHeight(),
                                shape = MaterialTheme.shapes.large,
                                tonalElevation = AlertDialogDefaults.TonalElevation
                            ) {
                                Text(text = "Удаление части...")
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
                                            text = "Часть удалена",
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