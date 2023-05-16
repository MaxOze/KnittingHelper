package com.example.knittinghelper.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.knittinghelper.R
import com.example.knittinghelper.domain.model.Part


@Composable
fun PartCardComponent(part: Part, navController: NavController) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(16.dp)
            .clickable(
                onClick = {
                    navController.navigate("projects/" + part.projectId + "/" + part.partId + "/")
                }
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Project Image",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = part.name,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Прогресс:",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.weight(1f))
                LinearProgressIndicator(
                    progress = (part.countRow / part.neededRow).toFloat(),
                    trackColor = Color.Red,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = part.countRow.toString() + "/" + part.neededRow.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
        }
    }
}