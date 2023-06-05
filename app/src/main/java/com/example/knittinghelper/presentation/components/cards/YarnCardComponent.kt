package com.example.knittinghelper.presentation.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.example.knittinghelper.domain.model.Yarn
import com.example.knittinghelper.presentation.profile.viewmodels.YarnViewModel

@Composable
fun YarnCardComponent(yarn: Yarn, delete: MutableState<String>, update: MutableState<String>, viewModel: YarnViewModel) {
    val menu = remember { mutableStateOf(false) }

    val count = remember { mutableStateOf<Int>(yarn.amount) }
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp, pressedElevation = 3.dp),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    if(yarn.photoUri != "") {
                        AsyncImage(
                            model = yarn.photoUri,
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
                    FilledTonalButton(
                        onClick = {
                            if (count.value > 0) {
                                count.value--
                                viewModel.updateYarn(yarn.yarnId, "", count.value)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Localized description"
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "${count.value} шт.", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(12.dp))
                    FilledTonalButton(
                        onClick = {
                            if (count.value < 99) {
                                count.value++
                                viewModel.updateYarn(yarn.yarnId, "", count.value)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Localized description"
                        )
                    }
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
                            text = { Text(text = "Изменить описание") },
                            onClick = {
                                menu.value = false
                                update.value = "${yarn.yarnId},${yarn.text}"
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "nav_icon",
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Удалить проект") },
                            onClick = {
                                menu.value = false
                                delete.value = yarn.yarnId
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
            Divider(thickness = 2.dp, modifier = Modifier.padding(horizontal = 12.dp))
            Text(
                text = "Материал: ${yarn.material}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 12.dp, start = 12.dp)
            )
            Text(
                text = "Цвет: ${yarn.color}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 6.dp, start = 12.dp, bottom = 6.dp)
            )
            Text(
                text = "Плотность: ${yarn.weight}/${yarn.length} г/м",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 12.dp, start = 12.dp)
            )
            Divider(thickness = 2.dp, modifier = Modifier.padding(horizontal = 12.dp))
            Text(
                text = yarn.text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}