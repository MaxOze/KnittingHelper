package com.example.knittinghelper.presentation.projects.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.knittinghelper.presentation.components.util.ChooseNeedleComponent
import com.example.knittinghelper.presentation.components.util.AddPhotoComponent
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu
import com.example.knittinghelper.presentation.projects.viewmodels.ProjectsViewModel
import com.example.knittinghelper.util.Needles
import com.example.knittinghelper.util.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(navController: NavController) {
    val viewModel: ProjectsViewModel = hiltViewModel()

    val name = remember { mutableStateOf("") }
    val needle = remember { mutableStateOf(Needles.CrochetHook.name) }
    val text = remember { mutableStateOf("") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val videoLink = remember { mutableStateOf("") }
    val simple = remember { mutableStateOf(false) }
    val neededRows = remember { mutableStateOf("0") }
    val videoError = remember { mutableStateOf(false) }
    val nameError = remember { mutableStateOf(false) }
    val buttonState = remember { mutableStateOf(false) }

    val createResponse = viewModel.createProjectData.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Создание проекта") },
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
                                    Text(text = "Создание проекта...")
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
                                        Text(text = "Создание проекта...")
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
                                    Text(text = "Не удалось создать проект!")
                                }
                            }
                        }
                    }
                }
            }
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
                                modifier = Modifier.size(10.dp)
                            )
                            Text(text = "*обязательное поле")
                        }
                        Text(text = "${name.value.length}/20")
                    }
                },
                label = { Text(text = "Название проекта") },
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
                label = { Text(text = "Описание проекта") },
                modifier = Modifier.fillMaxWidth()
            )
            AddPhotoComponent(imageUri)
            TextField(
                value = videoLink.value,
                onValueChange = { videoLink.value = it },
                isError = videoError.value,
                supportingText = {
                    if (videoError.value) {
                        Row {
                            Icon(
                                imageVector = Icons.Outlined.Error,
                                contentDescription = "error",
                                modifier = Modifier.size(10.dp)
                            )
                            Text(text = "Некорректная ссылка")
                        }
                    }
                },
                label = { Text(text = "Ссылка на видео (если есть)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlainTooltipBox(
                    tooltip = { Text("Проект состоит из одной части") }
                ) {
                    Checkbox(
                        checked = simple.value,
                        onCheckedChange = { simple.value = !simple.value }
                    )
                }
                Text(
                    text = "Проект из\nодной части",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(end = 16.dp)
                )
                TextField(
                    enabled = simple.value,
                    value = if (simple.value) neededRows.value else "0",
                    onValueChange = { neededRows.value = it },
                    supportingText = {
                        if (simple.value) Text(text = "*обязательное поле")
                    },
                    label = { Text(text = "Количество рядов") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Button(
                onClick = {
                    nameError.value = name.value.isEmpty()

                    val isValid = videoLink.value.matches(
                        Regex("^https?://(?:www\\.)?youtube\\.com/watch\\?v=([\\w-]+)(?:&[\\w-]+=[\\w-]+)*$")
                    )
                    if (videoLink.value.isNotEmpty())
                        videoError.value = !isValid
                    else
                        videoError.value = false

                    if (!nameError.value && !videoError.value) {
                        viewModel.createProject(
                            name = name.value,
                            text = text.value,
                            photoUri = imageUri.value,
                            videoUri = videoLink.value,
                            needle = needle.value,
                            simpleProject = simple.value,
                            neededRows = neededRows.value.toInt()
                        )
                        buttonState.value = true
                    } else buttonState.value = false
                },
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 40.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Создать проект")
            }
            Divider(modifier = Modifier
                .padding(bottom = 40.dp))
        }
    }
}
