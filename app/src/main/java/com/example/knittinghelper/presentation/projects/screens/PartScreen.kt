package com.example.knittinghelper.presentation.projects.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.knittinghelper.R
import com.example.knittinghelper.presentation.components.util.PhotoCards
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu
import com.example.knittinghelper.presentation.projects.viewmodels.PartViewModel
import com.example.knittinghelper.util.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel: PartViewModel = hiltViewModel()
    LaunchedEffect(true) {
        viewModel.getPartInfo()
    }

    val minusState = remember { mutableStateOf(false) }
    val plusState = remember { mutableStateOf(false) }
    val alert = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = { Text("Часть ${name.value}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "backToProjects"
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                ),
                actions = {
                    Row {
                        TextButton(
                            onClick = {
                                alert.value = true
                            }
                        ) {
                            Text(text = "Сбросить счетчик")
                            Icon(imageVector = Icons.Filled.Refresh, contentDescription = "clean progress")
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomNavigationMenu(navController = navController)
        }
    ) {
        when(val response = viewModel.getPartData.value) {
            is Response.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Snackbar(modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter)) {
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
            }
            is Response.Success -> {
                if (response.data != null) {
                    val part = response.data

                    var rows by remember { mutableStateOf<Int>(part.countRow) }

                    name.value = part.name
                    minusState.value = rows != 0
                    plusState.value = rows != part.neededRow

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                top = it.calculateTopPadding() + 16.dp,
                                start = 16.dp,
                                end = 16.dp
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if(part.photoUri != "") {
                                    AsyncImage(
                                        model = part.photoUri,
                                        contentDescription = "image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(CircleShape)
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.photo),
                                        contentDescription = "Project Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(CircleShape)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(text = part.name, style = MaterialTheme.typography.headlineSmall)
                            }
                            Divider(
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                val updateResponse = viewModel.updatePartProgress.value
                                FloatingActionButton(
                                    elevation = FloatingActionButtonDefaults.elevation(
                                        defaultElevation = if (updateResponse is Response.Loading) 0.dp else 4.dp
                                    ),
                                    onClick = {
                                        if(minusState.value && updateResponse !is Response.Loading) {
                                            rows--
                                            viewModel.updatePartProgress(part.countRow, rows)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Remove,
                                        contentDescription = "Localized description",
                                        modifier = Modifier.size(50.dp)
                                    )
                                }
                                Text(
                                    text = "$rows/${part.neededRow}")
                                FloatingActionButton(
                                    elevation = FloatingActionButtonDefaults.elevation(
                                        defaultElevation = if (updateResponse is Response.Loading) 0.dp else 4.dp
                                    ),
                                    onClick = {
                                        if(plusState.value && updateResponse !is Response.Loading) {
                                            rows++
                                            viewModel.updatePartProgress(part.countRow, rows)
                                        }

                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "Localized description",
                                        modifier = Modifier.size(50.dp)
                                    )
                                }
                            }
                            Divider(
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                            Text(text = part.text)
                            Divider(
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                            PhotoCards(photosUri = part.schemeUrls)
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }


                    if (alert.value) {
                        AlertDialog(onDismissRequest = { alert.value = false }) {
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
                                        text = "Точно хотите сбросить прогресс?",
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        TextButton(
                                            onClick = {
                                                rows = 0
                                                viewModel.updatePartProgress(part.countRow, rows)
                                                alert.value = false
                                            },
                                        ) {
                                            Text("Да")
                                        }
                                        TextButton(
                                            onClick = {
                                                alert.value = false
                                            },
                                        ) {
                                            Text("Нет")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            is Response.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
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
}

