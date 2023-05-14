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
import com.example.knittinghelper.presentation.profile.*
import com.example.knittinghelper.presentation.projects.*
import com.example.knittinghelper.presentation.social.*
import com.example.knittinghelper.presentation.util.SplashScreen
import com.example.knittinghelper.ui.theme.KnittingHelperTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KnittingHelperTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val authViewModel : AuthenticationViewModel = hiltViewModel()
                    KnittingHelperApp(navController, authViewModel)
                }
            }
        }
    }
}

@Composable
fun KnittingHelperApp(navController: NavHostController, authViewModel: AuthenticationViewModel) {
    NavHost(navController = navController, startDestination = Screens.SplashScreen.route) {
        // Аутентификация
        composable(route = Screens.SplashScreen.route) {
            SplashScreen(navController, authViewModel)
        }
        composable(route = Screens.SignInScreen.route) {
            SignInScreen(navController, authViewModel)
        }
        composable(route = Screens.SignUpScreen.route) {
            SignUpScreen(navController, authViewModel)
        }
        // Соцсеть
        composable(route = Screens.FeedScreen.route) {
            FeedScreen(navController)
        }
        // Проекты
        composable(route = Screens.ProjectsScreen.route) {
            ProjectsScreen(navController)
        }
        // Профиль
        composable(route = Screens.ProfileScreen.route) {
            ProfileScreen(navController, authViewModel)
        }
    }
}