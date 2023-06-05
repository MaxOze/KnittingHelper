package com.example.knittinghelper.presentation.components.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.knittinghelper.util.Needles


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseNeedleComponent(selectedText: MutableState<String>) {
    val needles = listOf(
        Needles.CrochetHook,
        Needles.CircularNeedles,
        Needles.StraightNeedle
    )
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, bottom = 16.dp)
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
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
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

@Composable
fun NeedlesButton(
    selectedType: MutableState<String>
) {

    Row(
        modifier = Modifier.padding(top = 10.dp, start = 25.dp, end = 25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selectedType.value == "") {
            Button(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = 2.dp,
                    top = 2.dp,
                    end = 2.dp,
                    bottom = 2.dp,
                ),
                shape = RoundedCornerShape(topStartPercent = 50, topEndPercent = 0, bottomStartPercent = 50, bottomEndPercent = 0),
                onClick = {  }
            ) {
                Text(text = "Все")
            }
        } else {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = 2.dp,
                    top = 2.dp,
                    end = 2.dp,
                    bottom = 2.dp,
                ),
                shape = RoundedCornerShape(topStartPercent = 50, topEndPercent = 0, bottomStartPercent = 50, bottomEndPercent = 0),
                onClick = { selectedType.value = "" }
            ) {
                Text(text = "Все")
            }
        }
        if (selectedType.value == Needles.StraightNeedle.name) {
            Button(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = 2.dp,
                    top = 2.dp,
                    end = 2.dp,
                    bottom = 2.dp,
                ),
                shape = RoundedCornerShape(0),
                onClick = {  }
            ) {
                Text(text = "Прямые")
            }
        } else {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = 2.dp,
                    top = 2.dp,
                    end = 2.dp,
                    bottom = 2.dp,
                ),
                shape = RoundedCornerShape(0),
                onClick = { selectedType.value = Needles.StraightNeedle.name }
            ) {
                Text(text = "Прямые")
            }
        }
        if (selectedType.value == Needles.CircularNeedles.name) {
            Button(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = 2.dp,
                    top = 2.dp,
                    end = 2.dp,
                    bottom = 2.dp,
                ),
                shape = RoundedCornerShape(0),
                onClick = {  }) {
                Text(text = "Круговые")
            }
        } else {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = 2.dp,
                    top = 2.dp,
                    end = 2.dp,
                    bottom = 2.dp,
                ),
                shape = RoundedCornerShape(0),
                onClick = { selectedType.value = Needles.CircularNeedles.name }) {
                Text(text = "Круговые")
            }
        }
        if (selectedType.value == Needles.CrochetHook.name) {
            Button(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = 2.dp,
                    top = 2.dp,
                    end = 2.dp,
                    bottom = 2.dp,
                ),
                shape = RoundedCornerShape(
                    topStartPercent = 0,
                    topEndPercent = 50,
                    bottomStartPercent = 0,
                    bottomEndPercent = 50
                ),
                onClick = {  }) {
                Text(text = "Крючок")
            }
        } else {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    start = 2.dp,
                    top = 2.dp,
                    end = 2.dp,
                    bottom = 2.dp,
                ),
                shape = RoundedCornerShape(
                    topStartPercent = 0,
                    topEndPercent = 50,
                    bottomStartPercent = 0,
                    bottomEndPercent = 50
                ),
                onClick = { selectedType.value = Needles.CrochetHook.name }) {
                Text(text = "Крючок")
            }
        }
    }
}

