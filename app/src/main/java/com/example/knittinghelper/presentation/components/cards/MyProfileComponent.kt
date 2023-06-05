package com.example.knittinghelper.presentation.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.knittinghelper.R
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.social.viewmodels.UserProfileViewModel


@Composable
fun MyProfile(userName: String, bio: String, photoUri: String, paddingValues: PaddingValues, navController: NavController) {
    val expanded = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding() + 12.dp,
                start = 12.dp,
                end = 12.dp,
                bottom = 12.dp
            )
        ) {
                Row{
                    if (photoUri != "") {
                        AsyncImage(
                            model = photoUri,
                            contentDescription = "image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.photo),
                            contentDescription = "Project Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Column() {
                    Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                    if (bio == "") {
                        Text(
                            text = "Добавьте описание к своему профилю в настройках!",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        Text(
                            text = bio,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { navController.navigate(Screens.YarnStockScreen.route) },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("Пряжа", style = MaterialTheme.typography.labelMedium)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { navController.navigate(Screens.NeedleStockScreen.route) },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("Спицы", style = MaterialTheme.typography.labelMedium)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { navController.navigate(Screens.SubsScreen.route) },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("Подписки", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
        }
    }
}

@Composable
fun Profile(userId: String, userName: String, bio: String, photoUri: String, paddingValues: PaddingValues, viewModel: UserProfileViewModel)  {
    val isSub = remember { mutableStateOf(viewModel.isSub) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding() + 12.dp,
                start = 12.dp,
                end = 12.dp,
                bottom = 12.dp
            )
        ) {
            Row {
                if (photoUri != "") {
                    AsyncImage(
                        model = photoUri,
                        contentDescription = "image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.photo),
                        contentDescription = "Project Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Column() {
                Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                if (bio == "") {
                    Text(
                        text = "Добавьте описание к своему профилю в настройках!",
                        maxLines = 2,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = bio,
                        maxLines = 2,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isSub.value == "true") {
                        FilledTonalButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.unsubscribe(userId)
                                isSub.value = "false"
                            }
                        ) {
                            Text(text = "Отписаться")
                        }
                    } else {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.subscribe(userId)
                                isSub.value = "true"
                            }
                        ) {
                            Text(text = "Подписаться")
                        }
                    }
                }
            }
        }
    }
}