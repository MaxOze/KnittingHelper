package com.example.knittinghelper.presentation.profile.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.components.cards.SubCardComponent
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu
import com.example.knittinghelper.presentation.profile.viewmodels.SubsViewModel
import com.example.knittinghelper.util.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubsScreen(navController: NavController) {
    val viewModel: SubsViewModel = hiltViewModel()

    LaunchedEffect(key1 = true) {
        viewModel.getUser()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Подписки") },
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
        when (val response = viewModel.getUserData.value) {
            is Response.Loading -> {
                Row(
                    modifier = Modifier.padding(top = 100.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            is Response.Success -> {
                if (response.data != null) {
                    val user = response.data
                    LaunchedEffect(key1 = true) {
                        if (user.following.isNotEmpty())
                            viewModel.getUserSubs(user.following)
                    }
                    when (val subsResponse = viewModel.getUserSubsData.value) {
                        is Response.Loading -> {
                            Row(
                                modifier = Modifier.padding(top = 100.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(50.dp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        is Response.Success -> {
                            if (subsResponse.data != null) {
                                val subs = subsResponse.data
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = it.calculateTopPadding() + 10.dp,
                                            start = 12.dp, end = 12.dp
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    items(subs) { sub ->
                                        SubCardComponent(user, sub, viewModel, navController)
                                        Divider(
                                            thickness = 1.dp,
                                            modifier = Modifier.padding(vertical = 10.dp)
                                        )
                                    }
                                    if (subs.size > 7) {
                                        item {
                                            Divider(
                                                thickness = 0.dp,
                                                modifier = Modifier.padding(top = 70.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            if (subsResponse.data == null && user.following.isEmpty()) {
                                Row(
                                    modifier = Modifier.padding(top = 100.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    TextButton(onClick = { navController.navigate("social_graph") }) {
                                        Text(
                                            text = "Найдите новых друзей!",
                                            style = MaterialTheme.typography.titleLarge
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
                                    Text(text = subsResponse.message)
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
                        Text(text = response.message)
                    }
                }
            }
        }
    }
}