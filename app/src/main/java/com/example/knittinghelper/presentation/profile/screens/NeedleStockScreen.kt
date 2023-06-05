package com.example.knittinghelper.presentation.profile.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.components.cards.NeedleCardComponent
import com.example.knittinghelper.presentation.components.cards.YarnCardComponent
import com.example.knittinghelper.presentation.components.util.NeedlesButton
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu
import com.example.knittinghelper.presentation.profile.viewmodels.NeedleViewModel
import com.example.knittinghelper.presentation.profile.viewmodels.YarnViewModel
import com.example.knittinghelper.util.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeedleStockScreen(navController: NavController) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()
    val expandedFab = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val delete =  remember { mutableStateOf("") }
    val deleteSuccess =  remember { mutableStateOf<Int>(0) }

    val viewModel: NeedleViewModel = hiltViewModel()

    LaunchedEffect(deleteSuccess) {
        viewModel.getUserNeedles()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "backToProfile"
                        )
                    }
                },
                title = { Text("Склад инструментов") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screens.CreateNeedleScreen.route) },
                expanded = expandedFab.value,
                icon = { Icon(Icons.Filled.Add, "add icon") },
                text = { Text(text = "Добавить инструмент") },
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomNavigationMenu(navController = navController)
        }
    ) {
        when (val response = viewModel.getUserNeedlesData.value) {
            is Response.Loading -> {
                Snackbar(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(text = "Загрузка инструментов...")
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            is Response.Success -> {
                if (response.data != null) {
                    if (delete.value.isNotEmpty()) {
                        AlertDialog(
                            onDismissRequest = {
                                delete.value = ""
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
                                        text = "Удалить этот инструмент без возможности возврата?",
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        TextButton(
                                            onClick = {
                                                viewModel.deleteNeedle(delete.value)
                                                delete.value = ""
                                            },
                                        ) {
                                            Text("Да")
                                        }
                                        TextButton(
                                            onClick = {
                                                delete.value = ""
                                            },
                                        ) {
                                            Text("Нет")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    val type =  remember { mutableStateOf("") }

                    LazyColumn(
                        modifier = Modifier
                            .padding(top = it.calculateTopPadding()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            NeedlesButton(selectedType = type)
                        }

                        if (response.data.isEmpty()) {
                            item {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .padding(top = 50.dp)
                                        .fillMaxWidth()
                                ) {
                                    TextButton(onClick = { navController.navigate(Screens.CreateNeedleScreen.route) }) {
                                        Text(
                                            text = "Добавьте сюда свои инструменты\n" +
                                                    "чтобы за ними было удобно следить!",
                                            style = MaterialTheme.typography.titleLarge,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        } else {
                            val needles = response.data
                            items(needles) { needle ->
                                if (type.value == "" || needle.type == type.value )
                                    NeedleCardComponent(needle, delete)
                            }
                            if (needles.size > 5) {
                                item {
                                    Divider(
                                        thickness = 0.dp,
                                        modifier = Modifier.padding(top = 170.dp)
                                    )
                                }
                            }
                        }
                    }
                    when (val deleteResponse = viewModel.deleteNeedleData.value) {
                        is Response.Loading -> {
                            AlertDialog(onDismissRequest = { }) {
                                Surface(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .wrapContentHeight(),
                                    shape = MaterialTheme.shapes.large,
                                    tonalElevation = AlertDialogDefaults.TonalElevation
                                ) {
                                    Text(text = "Удаление инструмента...")
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
                                                text = "Инструмент удален",
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