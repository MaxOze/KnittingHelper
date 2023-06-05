package com.example.knittinghelper.presentation.components.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBarComponent(rows: Int, neededRows: Int) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Связано:", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,)
            Text(text = if (neededRows == 0) "0%" else "${100 * rows/neededRows}%", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,)
        }
        LinearProgressIndicator(
            progress = if (neededRows == 0) 0.0F else rows / neededRows.toFloat(),
            modifier = Modifier.fillMaxWidth()
                .height(20.dp).padding(vertical = 5.dp),
            color = Color(0xFF984062),
            backgroundColor = Color(0xFFFFB0C9)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Осталось рядов: ${neededRows - rows}", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,)
        }
        Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp))
    }
}