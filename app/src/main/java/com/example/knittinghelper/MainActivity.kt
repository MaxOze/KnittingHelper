package com.example.knittinghelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.knittinghelper.presentation.navigation.NavigationGraph
import com.example.knittinghelper.ui.theme.KnittingHelperTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KnittingHelperTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavigationGraph()
                }
            }
        }
    }
}