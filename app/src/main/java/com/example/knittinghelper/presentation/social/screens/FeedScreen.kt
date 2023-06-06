package com.example.knittinghelper.presentation.social.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.knittinghelper.presentation.navigation.BottomNavigationMenu
import com.example.knittinghelper.presentation.social.viewmodels.SocialViewModel
import com.example.knittinghelper.util.Response
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.knittinghelper.R
import com.example.knittinghelper.domain.model.Post
import com.example.knittinghelper.presentation.components.cards.PostCardComponent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val viewModel: SocialViewModel = hiltViewModel()

    val listState = rememberLazyListState()
    val search = remember { mutableStateOf("") }
    val searchBool = remember { mutableStateOf(false) }
    val update = remember { mutableStateOf<Int>(0) }
    val prevSize = remember { mutableStateOf<Int>(0) }

    LaunchedEffect(true) {
        viewModel.getUserInfo()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = { Text("Сообщество") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        },
        bottomBar = {
            BottomNavigationMenu(navController = navController)
        }
    ) {
        when (val userResponse = viewModel.getUserData.value) {
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
                            Text(text = "Загрузка постов...")
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            is Response.Success -> {
                if (userResponse.data != null) {
                    LaunchedEffect(update) {
                        if (userResponse.data.following.isNotEmpty())
                            viewModel.GetPostsFeed()
                    }
                    when (val response = viewModel.getPostsFeedData.value) {
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
                                        Text(text = "Загрузка постов...")
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
                                if (response.data.size != prevSize.value && !listState.canScrollForward) {
                                    prevSize.value = response.data.size
                                    update.value++
                                }
                            }
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.padding(top = it.calculateTopPadding()),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp, horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        OutlinedTextField(
                                            singleLine = true,
                                            label = {
                                                Text(text = "Поиск пользователей", style = MaterialTheme.typography.bodySmall)
                                            },
                                            modifier = Modifier.weight(6.5f),
                                            value = search.value,
                                            onValueChange = {
                                                search.value = it
                                                searchBool.value = false
                                                viewModel.getUserUndo()
                                            },
                                            leadingIcon = {
                                                Icon(Icons.Outlined.Search, "")
                                            },
                                        )
                                        Spacer(modifier = Modifier.weight(0.5f),)
                                        Button(
                                            modifier = Modifier.weight(3f),
                                            onClick = { searchBool.value = true }) {
                                            Text(text = "Найти")
                                        }
                                    }
                                }
                                if (search.value != "" && searchBool.value) {
                                    viewModel.getUsers(search.value)
                                    when (val usersResponse = viewModel.getUsersData.value) {
                                        is Response.Loading -> {
                                            item {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(100.dp)
                                                        .padding(25.dp)
                                                )
                                            }
                                        }

                                        is Response.Success -> {
                                            items(usersResponse.data) { user ->
                                                Column(
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Divider(modifier = Modifier.fillMaxWidth())
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(
                                                                vertical = 5.dp,
                                                                horizontal = 12.dp
                                                            ).clickable(
                                                                onClick = {
                                                                    if (user.following.contains(user.userId))
                                                                        navController.navigate("social/${user.userId}/true")
                                                                    else
                                                                        navController.navigate("social/${user.userId}/false")
                                                                }
                                                            ),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        if (user.imageUri != "") {
                                                            AsyncImage(
                                                                model = user.imageUri,
                                                                contentDescription = "image",
                                                                contentScale = ContentScale.Crop,
                                                                modifier = Modifier
                                                                    .size(50.dp)
                                                                    .clip(CircleShape)
                                                            )
                                                        } else {
                                                            Image(
                                                                painter = painterResource(id = R.drawable.ic_launcher_background),
                                                                contentDescription = "Project Image",
                                                                contentScale = ContentScale.Crop,
                                                                modifier = Modifier
                                                                    .size(50.dp)
                                                                    .clip(CircleShape)
                                                            )
                                                        }
                                                        Spacer(modifier = Modifier.width(12.dp))
                                                        Text(
                                                            text = user.userName,
                                                            style = MaterialTheme.typography.bodyLarge
                                                        )
                                                    }
                                                    Divider(modifier = Modifier.fillMaxWidth())
                                                }
                                            }
                                        }

                                        is Response.Error -> {
                                            item {
                                                Text(
                                                    modifier = Modifier.padding(top = 25.dp),
                                                    text = "Ошибка!"
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    if (response.data != null) {
                                        items(response.data) { post ->
                                            PostCardComponent(
                                                post,
                                                navController,
                                                false,
                                                userResponse.data
                                            )
                                        }
                                        if (prevSize.value == response.data.size) {
                                            item {
                                                Text(
                                                    text = "Вы долистали до конца!",
                                                    modifier = Modifier.padding(top = 12.dp)
                                                )
                                            }
                                        }
                                        item {
                                            Divider(
                                                modifier = Modifier.padding(top = 150.dp)
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
                                        Text(text = "Не удалось загрузить проекты!")
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        TextButton(onClick = { viewModel.undo() }) {
                                            Text(text = "Ok")
                                        }
                                    }
                                }
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
                            Text(text = "Ошибка!")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { viewModel.undo() }) {
                                Text(text = "Ok")
                            }
                        }
                    }
                }
            }
        }
    }
}