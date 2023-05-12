package com.example.knittinghelper.presentation

sealed class Screens(val route: String) {
    // Вспомогательные экраны
    object SplashScreen : Screens("splash_screen")

    // Экраны аутентификации
    object SignInScreen : Screens("signin_screen")
    object SignUpScreen : Screens("signup_screen")

    // Экраны профиля
    object ProfileScreen : Screens("profile_screen")

    // Экраны проектов
    object  ProjectsScreen : Screens("projects_screen")

    // Экраны соцсети
}