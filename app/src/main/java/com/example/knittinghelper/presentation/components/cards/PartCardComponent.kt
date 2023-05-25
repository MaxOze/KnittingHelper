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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.knittinghelper.R
import com.example.knittinghelper.domain.model.Part


@Composable
fun PartCardComponent(delete: MutableState<Part?>, count: Int, part: Part, navController: NavController) {
    val menu = remember { mutableStateOf(false) }
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp, pressedElevation = 3.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(16.dp)
            .clickable(
                onClick = {
                    navController.navigate("projects/" + part.projectId + "/" + part.partId + "/" + count.toString() + "/")
                }
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if(part.photoUri != "") {
                        AsyncImage(
                            model = part.photoUri,
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
                        text = part.name,
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
                                delete.value = part
                            },
                            leadingIcon = { Icons.Outlined.DeleteOutline}
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Прогресс:",
                    style = MaterialTheme.typography.titleMedium,
                )
                LinearProgressIndicator(
                    progress = (part.countRow / part.neededRow).toFloat(),
                    trackColor = Color.Red,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = part.countRow.toString() + "/" + part.neededRow.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
        }
    }
}