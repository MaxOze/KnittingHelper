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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.knittinghelper.R
import com.example.knittinghelper.domain.model.Part
import com.example.knittinghelper.presentation.components.util.ProgressBarComponent


@Composable
fun PartCardComponent(projectId: String, delete: MutableState<Part?>, count: Int, part: Part, navController: NavController) {
    val menu = remember { mutableStateOf(false) }
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp, pressedElevation = 3.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .clickable(
                onClick = {
                    navController.navigate("projects/" + projectId + "/" + part.partId + "/" + count.toString() + "/")
                }
            )
    ) {
        Column(
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if(part.photoUri != "") {
                        AsyncImage(
                            model = part.photoUri,
                            contentDescription = "image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.photo),
                            contentDescription = "Project Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = part.name,
                        style = MaterialTheme.typography.titleMedium
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
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.DeleteOutline,
                                    contentDescription = "nav_icon",
                                )
                            }
                        )
                    }
                }
            }
            ProgressBarComponent(rows = part.countRow, neededRows = part.neededRow)
        }
    }
}