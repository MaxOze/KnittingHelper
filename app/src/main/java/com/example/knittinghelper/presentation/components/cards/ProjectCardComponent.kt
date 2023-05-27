package com.example.knittinghelper.presentation.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.knittinghelper.R
import com.example.knittinghelper.domain.model.Project
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProjectCardComponent(delete: MutableState<String>, project: Project, navController: NavController) {
    val menu = remember { mutableStateOf(false) }

    val date = Date(project.lastUpdate.seconds * 1000L)
    val format = SimpleDateFormat("HH:mm dd/MM/yy", Locale.getDefault())
    val dateString = format.format(date)
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp, pressedElevation = 3.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .clickable(
                onClick = {
                    navController.navigate("projects/" + project.projectId + "/")
                }
            )
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)) {
                    if(project.photoUri != "") {
                        AsyncImage(
                            model = project.photoUri,
                            contentDescription = "image",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = "Project Image",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = project.name,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Box {
                    IconButton(onClick = {
                        menu.value = !menu.value
                    }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Localized description")
                    }
                    DropdownMenu(
                        expanded = menu.value,
                        onDismissRequest = {
                            menu.value = false
                        },
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Удалить проект") },
                            onClick = {
                                menu.value = false
                                delete.value = project.projectId
                            },
                            leadingIcon = { Icons.Outlined.DeleteOutline}
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Прогресс:",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.width(12.dp))
                LinearProgressIndicator(
                    progress = if (project.neededRows == 0) 0.0F else project.countRows / project.neededRows.toFloat(),
                    trackColor = Color.Red,
                    modifier = Modifier.weight(1f).padding(top = 3.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Последнее изменение:",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = dateString,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
        }
    }
}

@Composable
fun ProjectCardComponent(pad: PaddingValues,  project: Project) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier.padding(
                top = pad.calculateTopPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (project.photoUri != "") {
                    AsyncImage(
                        model = project.photoUri,
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
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Прогресс:",
                    style = MaterialTheme.typography.titleMedium,
                )
                LinearProgressIndicator(
                    progress = if (project.neededRows == 0) 0.0F else project.countRows / project.neededRows.toFloat(),
                    trackColor = Color.Red,
                    modifier = Modifier.padding(start = 20.dp, end = 10.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = project.text)
        }
    }
}