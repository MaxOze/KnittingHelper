package com.example.knittinghelper.presentation.profile.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
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
import com.example.knittinghelper.presentation.components.util.AddPhotoComponent
import com.example.knittinghelper.presentation.components.util.ChooseNeedleComponent
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu
import com.example.knittinghelper.presentation.profile.viewmodels.NeedleViewModel
import com.example.knittinghelper.presentation.profile.viewmodels.YarnViewModel
import com.example.knittinghelper.util.Needles
import com.example.knittinghelper.util.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateYarnScreen(navController: NavController) {
    val viewModel: YarnViewModel = hiltViewModel()

    val color = remember { mutableStateOf("") }
    val material = remember { mutableStateOf("") }
    val weight = remember { mutableStateOf("1") }
    val length = remember { mutableStateOf("1") }
    val amount = remember { mutableStateOf("1") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val createResponse = viewModel.createYarnData.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Добавление пряжи") },
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
                                Text(text = "Добавление пряжи...")
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
                                    Text(text = "Создание пряжи...")
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
                                Text(text = "Не удалось добавить пряжу!")
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

            TextField(
                value = color.value,
                onValueChange = { color.value = it },
                label = { Text(text = "Цвет") },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            )
            TextField(
                value = material.value,
                onValueChange = { material.value = it },
                label = { Text(text = "Материал") },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            )
            AddPhotoComponent(imageUri)
            Row(
                modifier = Modifier.padding(vertical = 12.dp)
            ){
                TextField(
                    value = weight.value,
                    onValueChange = { weight.value = it },
                    supportingText = {
                        Text(text = "в граммах")
                    },
                    label = { Text(text = "вес") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.width(10.dp))
                TextField(
                    value = length.value,
                    onValueChange = { length.value = it },
                    supportingText = {
                        Text(text = "в метрах")
                    },
                    label = { Text(text = "Длина") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.width(10.dp))
                TextField(
                    value = amount.value,
                    onValueChange = { amount.value = it },
                    supportingText = {
                        Text(text = "в штуках")
                    },
                    label = { Text(text = "Кол-во") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Button(
                onClick = {
                    viewModel.createYarn(
                        color = color.value,
                        material = material.value,
                        weight = weight.value.toInt(),
                        length = length.value.toInt(),
                        amount = amount.value.toInt(),
                        photoUri = imageUri.value
                    )

                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Добавить пряжу")
            }
        }
    }
}