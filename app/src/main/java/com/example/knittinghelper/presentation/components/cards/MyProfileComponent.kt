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
import coil.compose.AsyncImage
import com.example.knittinghelper.R


@Composable
fun MyProfile(userName: String, bio: String, photoUri: String, paddingValues: PaddingValues) {
    val expanded = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { if(bio != "") expanded.value = !expanded.value }),
    ) {
        Column(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
        ) {
            if (!expanded.value) {
                Row(modifier = Modifier.padding(12.dp)) {
                    if (photoUri != "") {
                        AsyncImage(
                            model = photoUri,
                            contentDescription = "image",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = "Project Image",
                            modifier = Modifier
                                .size(48.dp)
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
                Column(modifier = Modifier.padding(start = 12.dp, end = 12.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))
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
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("Пряжа", style = MaterialTheme.typography.labelMedium)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("Спицы", style = MaterialTheme.typography.labelMedium)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1.5f),
                        ) {
                            Text("Подписки", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Divider(
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = bio,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}