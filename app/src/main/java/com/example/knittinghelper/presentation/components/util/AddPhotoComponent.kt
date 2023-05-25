package com.example.knittinghelper.presentation.components.util

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.knittinghelper.R


@Composable
fun AddPhotoComponent(selectedPhoto: MutableState<Uri?>) {
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        selectedPhoto.value = it
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).height(200.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if(selectedPhoto.value != null) {
            AsyncImage(
                model = selectedPhoto.value,
                contentDescription = "kek",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(200.dp)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "kek",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(200.dp)
            )
        }
        Button(
            onClick = { galleryLauncher.launch("image/*") },
        ) {
            Text(text = "Выбрать фото")
        }
    }
}


@Composable
fun AddPhotosComponent(selectedPhotos: MutableState<List<Uri?>>) {
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) {
        selectedPhotos.value = it
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if(selectedPhotos.value.isNotEmpty()) {
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(selectedPhotos.value.count()) {
                    selectedPhotos.value.forEach { photo ->
                        AsyncImage(
                            model = photo,
                            contentDescription = "kek",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.padding(16.dp, 8.dp)
                                .size(100.dp)
                        )
                    }
                }
            }
        }
        Button(
            onClick = { galleryLauncher.launch("image/*") },
        ) {
            Text(text = "Выбрать фото")
        }
    }
}