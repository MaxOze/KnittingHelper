package com.example.knittinghelper.presentation.profile.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.knittinghelper.domain.model.User
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu
import com.example.knittinghelper.presentation.profile.viewmodels.ProfileViewModel
import com.example.knittinghelper.util.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel: ProfileViewModel = hiltViewModel()

    val update = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = update) {
        viewModel.getUserInfo()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки профиля") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
        when (val response = viewModel.getUserData.value) {
            is Response.Loading -> {
                Snackbar(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(text = "Загрузка профиля...")
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Black
                        )
                    }
                }
            }
            is Response.Success -> {
                if (response.data != null) {
                    val user = response.data
                    val name = remember{ mutableStateOf(user.userName) }
                    val nameError = remember{ mutableStateOf(false) }
                    val bio = remember{ mutableStateOf(user.bio) }
                    val photo = remember{ mutableStateOf(user.imageUri) }
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = it.calculateTopPadding() + 12.dp,
                                bottom = 12.dp,
                                start = 12.dp,
                                end = 12.dp
                            )
                    ) {
                        item {
                            Text(text = "Профиль", modifier = Modifier.padding(bottom = 12.dp))
                        }
                        item {
                            OutlinedTextField(
                                value = name.value,
                                onValueChange = { if (it.length <= 20) name.value = it },
                                singleLine = true,
                                isError = nameError.value,
                                supportingText = {
                                    Row {
                                        if (nameError.value) {
                                            Icon(
                                                imageVector = Icons.Outlined.Error,
                                                contentDescription = "error",
                                                modifier = Modifier.size(10.dp)
                                            )
                                        }
                                        Text(text = "*обязательное поле")
                                        Text(text = "${name.value.length}/20")
                                    }
                                },
                                label = {
                                    Text(text = "Введите имя:")
                                }
                            )
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
                        Text(text = response.message)
                    }
                }
            }
        }
    }
}