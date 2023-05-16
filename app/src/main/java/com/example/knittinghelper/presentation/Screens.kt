package com.example.knittinghelper.presentation

sealed class Screens(val route: String) {
    // Вспомогательные экраны
    object SplashScreen : Screens("splash/")

    // Экраны аутентификации
    object SignInScreen : Screens("signin/")
    object SignUpScreen : Screens("signup/")

    // Экраны профиля
    object ProfileScreen : Screens("profile/")
    object SettingsScreen : Screens("profile/settings/")    // сделать
    object YarnStockScreen : Screens("profile/yarn/")
    object NeedleStockScreen : Screens("profile/needle/")
    object SubsScreen : Screens("profile/subs/")

    // Экраны проектов
    object ProjectsScreen : Screens("projects/")
    object ProjectScreen : Screens("projects/{projectId}/")
    object PartScreen : Screens("projects/{projectId}/{partId}/")
    object CreateProjectScreen : Screens("projects-create/")    // сделать
    object CreatePartScreen : Screens("projects-create/{projectId}/")   // сделать
    object UpdateProjectScreen : Screens("projects-update/{projectId}/")
    object UpdatePartScreen : Screens("projects-update/{projectId}/{partId}")


    // Экраны соцсети
    object FeedScreen : Screens("social/")
}