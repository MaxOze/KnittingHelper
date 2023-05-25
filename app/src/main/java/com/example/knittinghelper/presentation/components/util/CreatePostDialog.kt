package com.example.knittinghelper.presentation.components.util

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.knittinghelper.presentation.profile.viewmodels.ProfileViewModel
import com.example.knittinghelper.util.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostDialog(
    profileViewModel: ProfileViewModel,
    create: MutableState<Boolean>,
    userName: String,
    photoUri: String
) {
    val needle = remember { mutableStateOf("") }
    val text = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { create.value = false },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            ChooseNeedleComponent(selectedText = needle)
            TextField(
                value = text.value,
                onValueChange = { if (it.length <= 100) text.value = it },
                supportingText = {
                    Row(horizontalArrangement = Arrangement.End) {
                        Text(text = "")
                    }
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { create.value = false }
                ) {
                    Text(text = "Отмена")
                }
                Button(
                    onClick = {
                        profileViewModel.createPost(
                            userName,
                            photoUri,
                            null,
                            text.value,
                            needle.value
                        )
                    }
                ) {
                    when (val response = profileViewModel.createPostData.value) {
                        is Response.Loading -> {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        is Response.Success -> {
                            if (response.data) {
                                create.value = false
                            } else {
                                Text(text = "Опубликовать")
                            }
                        }
                        is Response.Error -> {
                            Text(text = "Try again")
                        }
                    }

                }
            }
        }
    }
}