package com.example.knittinghelper.presentation.profile.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.knittinghelper.presentation.components.cards.PostCardComponent
import com.example.knittinghelper.presentation.components.cards.Profile
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu
import com.example.knittinghelper.presentation.social.viewmodels.UserProfileViewModel
import com.example.knittinghelper.util.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val userProfileViewModel: UserProfileViewModel = hiltViewModel()

    val snackbarHostState = remember { SnackbarHostState() }
    val sub = remember { mutableStateOf(userProfileViewModel.isSub) }

    LaunchedEffect(key1 = true) {
        userProfileViewModel.getUserInfo()
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "backToProjects"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                title = { Text("Профиль") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                ),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomNavigationMenu(navController = navController)
        }
    ) {
        when (val response = userProfileViewModel.getUserProfileData.value) {
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
                    LaunchedEffect(key1 = true) {
                        userProfileViewModel.getUserPosts()
                    }

                    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                        item {
                            Profile(user.userId, user.userName, user.bio, user.imageUri, it, userProfileViewModel)
                        }
                        when (val postsResponse = userProfileViewModel.getUserPostsData.value) {
                            is Response.Loading -> {
                                item {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(100.dp),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            is Response.Success -> {
                                if(postsResponse.data != null) {
                                    val posts = postsResponse.data
                                    items(posts) { post ->
                                        PostCardComponent(post, navController, true, user)
                                    }
                                    item {
                                        Divider(
                                            thickness = 0.dp,
                                            modifier = Modifier.padding(top = 170.dp)
                                        )
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