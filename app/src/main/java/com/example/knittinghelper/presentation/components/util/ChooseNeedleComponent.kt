package com.example.knittinghelper.presentation.components.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.knittinghelper.util.Needles


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseNeedleComponent(selectedText: MutableState<String>) {
    val needles = listOf(
        Needles.CrochetHook,
        Needles.CircularNeedles,
        Needles.StockingNeedle,
        Needles.StraightNeedle
    )
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp ,bottom = 16.dp)
    ) {
        TextField(
            value = selectedText.value,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "Инструмент") },
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                )
            },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            needles.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.name) },
                    trailingIcon = {
                        item.icon
                    },
                    onClick = {
                        selectedText.value = item.name
                        expanded = false
                    }
                )
            }
        }
    }
}
