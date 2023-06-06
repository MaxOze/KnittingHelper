package com.example.knittinghelper.presentation.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Remove
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.knittinghelper.R
import com.example.knittinghelper.domain.model.Project
import com.example.knittinghelper.presentation.components.util.ProgressBarComponent
import com.example.knittinghelper.presentation.projects.viewmodels.PartViewModel
import com.example.knittinghelper.presentation.projects.viewmodels.ProjectViewModel
import com.example.knittinghelper.util.Needles
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProjectCardComponent(delete: MutableState<String>, project: Project, navController: NavController) {
    val menu = remember { mutableStateOf(false) }

    val date = Date(project.lastUpdate.seconds * 1000L)
    val format = SimpleDateFormat("HH:mm dd/MM/yy", Locale.getDefault())
    format.timeZone = Calendar.getInstance().timeZone;
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
                            painter = painterResource(id = R.drawable.photo),
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
                                delete.value = project.projectId
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
            ProgressBarComponent(rows = project.countRows, neededRows = project.neededRows)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = Needles.chooseNeedle(project.needle).icon,
                    contentDescription = "image",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Последнее изменение:",
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = dateString,
                    style = MaterialTheme.typography.bodyLarge,
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
                bottom = 4.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (project.photoUri != "") {
                    AsyncImage(
                        model = project.photoUri,
                        contentDescription = "image",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.photo),
                        contentDescription = "Project Image",
                        modifier = Modifier
                            .size(60.dp)
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
            ProgressBarComponent(rows = project.countRows, neededRows = project.neededRows)
            Text(text = project.text, modifier = Modifier.padding(horizontal = 12.dp))
            Divider(
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp)
            )
            if (project.videoUri != "") {
                Text(text = "Ссылка: ${project.videoUri}", modifier = Modifier.padding(horizontal = 12.dp))
                Divider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp)
                )
            }
        }
    }
}