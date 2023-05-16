package com.example.knittinghelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.knittinghelper.presentation.Screens
import com.example.knittinghelper.presentation.auth.*
import com.example.knittinghelper.presentation.navigation.NavigationGraph
import com.example.knittinghelper.presentation.profile.*
import com.example.knittinghelper.presentation.projects.*
import com.example.knittinghelper.presentation.social.*
import com.example.knittinghelper.presentation.navigation.SplashScreen
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