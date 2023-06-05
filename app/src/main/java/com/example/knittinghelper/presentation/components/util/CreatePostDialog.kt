package com.example.knittinghelper.presentation.components.util

import android.net.Uri
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
    padding: PaddingValues,
    profileViewModel: ProfileViewModel,
    create: MutableState<Boolean>,
    userName: String,
    photoUri: String
) {
    val needle = remember { mutableStateOf("") }
    val text = remember { mutableStateOf("Описание") }
    val photos = remember { mutableStateOf<List<Uri?>>(emptyList()) }

    AlertDialog(
        onDismissRequest = { create.value = false },
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding(),
            )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Создание поста",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                ChooseNeedleComponent(selectedText = needle)
                TextField(
                    minLines = 8,
                    maxLines = 8,
                    value = text.value,
                    onValueChange = { if (it.length <= 200) text.value = it },
                    supportingText = {
                        Row(horizontalArrangement = Arrangement.End) {
                            Text(text = "")
                        }
                    }
                )
                AddPhotosComponent(selectedPhotos = photos)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { create.value = false }
                    ) {
                        Text(text = "Отмена")
                    }
                    Button(
                        onClick = {
                            profileViewModel.createPost(
                                userName,
                                photoUri,
                                photos.value,
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
                                    profileViewModel.undoCreatePost()
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
}