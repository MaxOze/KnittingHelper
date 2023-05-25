package com.example.knittinghelper.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.knittinghelper.R

sealed class Needles(val name: String, val icon: ImageVector) {
    object CrochetHook : Needles("Крючок", Icons.Default.Info)
    object StraightNeedle : Needles("Обычные спицы", Icons.Default.Info)
    object StockingNeedle : Needles("Чулочные спицы", Icons.Default.Info)
    object CircularNeedles : Needles("Круговые спицы", Icons.Default.Info)
}
