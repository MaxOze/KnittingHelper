package com.example.knittinghelper.util

import android.graphics.drawable.Drawable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.knittinghelper.R

sealed class Needles(val name: String, val icon: Int) {
    object CrochetHook : Needles("Крючок", R.drawable.crochet)
    object StraightNeedle : Needles("Прямые спицы", R.drawable.needle)
    object CircularNeedles : Needles("Круговые спицы", R.drawable.circle)

    companion object Factory {
        fun chooseNeedle(name: String) : Needles {
            return when (name) {
                "Крючок" -> Needles.CrochetHook
                "Прямые спицы" -> Needles.StraightNeedle
                else -> Needles.CircularNeedles
            }
        }
    }
}


