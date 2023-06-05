package com.example.knittinghelper.presentation.components.cards

import android.graphics.Paint.Style
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.knittinghelper.domain.model.User
import com.example.knittinghelper.presentation.profile.viewmodels.SubsViewModel


@Composable
fun SubCardComponent(user: User, subUser: User, viewModel: SubsViewModel, navController: NavController) {
    val sub = remember { mutableStateOf(true) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(
                onClick = {
                    if (user.following.contains(subUser.userId))
                        navController.navigate("profile/subs/${subUser.userId}/true")
                    else
                        navController.navigate("profile/subs/${subUser.userId}/false")
                }
            )
        ) {
            if(subUser.imageUri != "") {
                AsyncImage(
                    model = subUser.imageUri,
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
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = subUser.userName, style = MaterialTheme.typography.bodyMedium)
        }
        if (sub.value) {
            Button(
                onClick = {
                    viewModel.unsubscribe(subUser.userId)
                    sub.value = !sub.value
                }
            ) {
                Text(text = "Отписаться")
            }
        } else {
            FilledTonalButton(
                onClick = {
                    viewModel.subscribe(subUser.userId)
                    sub.value = !sub.value
                }
            ) {
                Text(text = "Отмена")
            }
        }

    }
}