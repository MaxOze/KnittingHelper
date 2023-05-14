package com.example.knittinghelper.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.auth.AuthenticationViewModel
import com.example.knittinghelper.presentation.components.MyProfile
import com.example.knittinghelper.presentation.util.BottomNavigationMenu
import com.example.knittinghelper.util.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, authenticationViewModel: AuthenticationViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val profileViewModel: ProfileViewModel = hiltViewModel()
    profileViewModel.getUserInfo()
    when(val response = profileViewModel.getUserData.value) {
        is Response.Loading -> {
            CircularProgressIndicator()
        }
        is Response.Success -> {
            if(response.data != null) {
                val user = response.data
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
                                        navController.navigate(Screens.ProfileScreen.route)
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
                                    when(val responseOut = authenticationViewModel.signOutState.value) {
                                        is Response.Loading ->{ }
                                        is Response.Success ->{
                                            if(responseOut.data){
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
                                        is Response.Error ->{
                                            Toast.makeText(LocalContext.current, responseOut.message, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomNavigationMenu(selectedItem = 2, navController = navController)
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        MyProfile(user.userName, user.bio, it)
                    }
                }
            }
        }
        is Response.Error -> {
            Toast.makeText(LocalContext.current, response.message, Toast.LENGTH_SHORT).show()
        }
    }
}