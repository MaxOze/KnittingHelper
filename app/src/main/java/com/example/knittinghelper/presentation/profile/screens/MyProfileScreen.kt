package com.example.knittinghelper.presentation.profile.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.auth.AuthenticationViewModel
import com.example.knittinghelper.presentation.components.cards.MyProfile
import com.example.knittinghelper.presentation.components.cards.PostCardComponent
import com.example.knittinghelper.presentation.components.util.CreatePostDialog
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu
import com.example.knittinghelper.presentation.profile.viewmodels.ProfileViewModel
import com.example.knittinghelper.util.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val authenticationViewModel: AuthenticationViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()

    val listState = rememberLazyListState()
    val expandedFab = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val create = remember { mutableStateOf(false) }
    val delete = remember { mutableStateOf("") }
    val deleteSuccess = remember { mutableStateOf(0) }

    LaunchedEffect(key1 = true) {
        profileViewModel.getUserInfo()
    }

    LaunchedEffect(deleteSuccess) {
        profileViewModel.getUserPosts()
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = { Text("Профиль") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                ),
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screens.SettingsScreen.route)
                        }
                    ) {
                        Icon(Icons.Filled.Settings, contentDescription = "settings")
                    }
                    IconButton(
                        onClick = {
                            authenticationViewModel.signOut()
                        }
                    ) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "exit")
                        when (val responseOut = authenticationViewModel.signOutState.value) {
                            is Response.Loading -> {}
                            is Response.Success -> {
                                if (responseOut.data) {
                                    authenticationViewModel.signOutEnd()
                                    LaunchedEffect(key1 = true) {
                                        navController.navigate(Screens.SignInScreen.route) {
                                            popUpTo(Screens.ProfileScreen.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            }
                            is Response.Error -> {
                                Toast.makeText(
                                    LocalContext.current,
                                    responseOut.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { create.value = true },
                expanded = expandedFab.value,
                icon = { Icon(Icons.Filled.Add, "add icon") },
                text = { Text(text = "Создать новый пост") },
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomNavigationMenu(navController = navController)
        }
    ) {
        when (val response = profileViewModel.getUserData.value) {
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
                    if (create.value) {
                        CreatePostDialog(it, profileViewModel, create, user.userName, user.imageUri)
                    }
                    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                        item {
                            MyProfile(user.userName, user.bio, user.imageUri, it, navController)
                        }
                        when (val postsResponse = profileViewModel.getUserPosts.value) {
                            is Response.Loading -> {
                                item {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(100.dp),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            is Response.Success -> {
                                val posts = postsResponse.data
                                if (!posts.isNullOrEmpty()) {
                                    items(posts) { post ->
                                        PostCardComponent(post, navController, true, user)
                                    }
                                    item {
                                        Divider(
                                            thickness = 0.dp,
                                            modifier = Modifier.padding(top = 170.dp)
                                        )
                                    }
                                } else {
                                    item {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier
                                                .padding(top = 50.dp)
                                                .fillMaxWidth()
                                        ) {
                                            TextButton(onClick = { create.value = true }) {
                                                Text(
                                                    text = "Создайте первый пост!",
                                                    style = MaterialTheme.typography.titleLarge
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            is Response.Error -> {
                                item {
                                    Text(text = "Не получилось\nзагрузить проекты")
                                }
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