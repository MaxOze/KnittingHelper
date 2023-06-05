package com.example.knittinghelper.presentation.projects.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.knittinghelper.domain.model.Project
import com.example.knittinghelper.presentation.components.util.AddPhotoComponent
import com.example.knittinghelper.presentation.components.util.AddPhotosComponent
import com.example.knittinghelper.presentation.components.util.ChooseNeedleComponent
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu
import com.example.knittinghelper.presentation.projects.viewmodels.ProjectViewModel
import com.example.knittinghelper.util.Needles
import com.example.knittinghelper.util.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePartScreen(navController: NavController) {
    val viewModel: ProjectViewModel = hiltViewModel()

    LaunchedEffect(key1 = true) {
        viewModel.getProjectInfo()
    }

    val response = viewModel.getProjectData.value

    val name = remember { mutableStateOf("") }
    val needle = remember { mutableStateOf(Needles.CrochetHook.name) }
    val text = remember { mutableStateOf("") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val neededRows = remember { mutableStateOf("1") }
    val nameError = remember { mutableStateOf<Boolean>(false) }
    val buttonState = remember { mutableStateOf(false) }
    val photos = remember { mutableStateOf<List<Uri?>>(emptyList()) }

    val createResponse = viewModel.createPartData.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Создание части проекта") },
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
                )
            )
        },
        bottomBar = {
            BottomNavigationMenu(navController = navController)
        }
    ) {
        when (response) {
            is Response.Loading -> {
                AlertDialog(
                    onDismissRequest = { }
                ) {
                    Surface(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        shape = MaterialTheme.shapes.large,
                        tonalElevation = AlertDialogDefaults.TonalElevation
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(text = "Загрузка...")
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            is Response.Success -> {
                if (response.data != null) {
                    if (buttonState.value) {
                        when (createResponse) {
                            is Response.Loading -> {
                                AlertDialog(
                                    onDismissRequest = { }
                                ) {
                                    Surface(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .wrapContentHeight(),
                                        shape = MaterialTheme.shapes.large,
                                        tonalElevation = AlertDialogDefaults.TonalElevation
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceAround
                                        ) {
                                            Text(text = "Создание части...")
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(16.dp),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }

                            is Response.Success -> {
                                if (createResponse.data) {
                                    LaunchedEffect(key1 = true) {
                                        navController.popBackStack()
                                    }
                                    AlertDialog(
                                        onDismissRequest = { }
                                    ) {
                                        Surface(
                                            modifier = Modifier
                                                .wrapContentWidth()
                                                .wrapContentHeight(),
                                            shape = MaterialTheme.shapes.large,
                                            tonalElevation = AlertDialogDefaults.TonalElevation
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceAround
                                            ) {
                                                Text(text = "Создание части...")
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(16.dp),
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            is Response.Error -> {
                                AlertDialog(
                                    onDismissRequest = { }
                                ) {
                                    Surface(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .wrapContentHeight(),
                                        shape = MaterialTheme.shapes.large,
                                        tonalElevation = AlertDialogDefaults.TonalElevation
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceAround
                                        ) {
                                            Text(text = "Не удалось создать часть!")
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            TextButton(onClick = { viewModel.createUndo() }) {
                                                Text(text = "Ok")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(
                                top = it.calculateTopPadding(),
                                start = 16.dp,
                                end = 16.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextField(
                            value = name.value,
                            onValueChange = { if (it.length <= 20) name.value = it },
                            supportingText = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row {
                                        if (nameError.value) Icon(
                                            imageVector = Icons.Outlined.Error,
                                            contentDescription = "error",
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(text = "*обязательное поле")
                                    }
                                    Text(text = "${name.value.length}/20")
                                }
                            },
                            label = { Text(text = "Название части") },
                            isError = nameError.value,
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        )
                        ChooseNeedleComponent(selectedText = needle)
                        TextField(
                            value = text.value,
                            onValueChange = { text.value = it },
                            label = { Text(text = "Описание части") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        AddPhotoComponent(imageUri)
                        AddPhotosComponent(selectedPhotos = photos)
                        TextField(
                            value = neededRows.value,
                            onValueChange = {
                                try {
                                    if (it.toInt() > 0) neededRows.value = it else "1"
                                } catch (e: Exception) {
                                    "1"
                                }
                            },
                            supportingText = { Text(text = "*обязательное поле") },
                            label = { Text(text = "Количество рядов") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Button(
                            onClick = {
                                nameError.value = name.value.isEmpty()

                                if (!nameError.value) {
                                    viewModel.createPart(
                                        name = name.value,
                                        text = text.value,
                                        photoUri = imageUri.value,
                                        schemeUri = photos.value,
                                        needle = needle.value,
                                        neededRow = neededRows.value.toInt(),
                                        projectRows = response.data.neededRows)

                                    buttonState.value = true
                                } else buttonState.value = false
                            },
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 40.dp)
                                .fillMaxWidth()
                        ) {
                            Text(text = "Создать часть")
                        }
                        Divider(
                            modifier = Modifier
                                .padding(bottom = 40.dp)
                        )
                    }
                }
            }

            is Response.Error -> {
                AlertDialog(
                    onDismissRequest = { }
                ) {
                    Surface(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        shape = MaterialTheme.shapes.large,
                        tonalElevation = AlertDialogDefaults.TonalElevation
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(text = response.message)
                        }
                    }
                }
            }
        }
    }
}

