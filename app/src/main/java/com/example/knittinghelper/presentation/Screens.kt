package com.example.knittinghelper.presentation

sealed class Screens(val route: String) {
    // Вспомогательные экраны
    object SplashScreen : Screens("splash/")

    // Экраны аутентификации
    object SignInScreen : Screens("signin/")
    object SignUpScreen : Screens("signup/")

    // Экраны профиля
    object MyProfileScreen : Screens("profile/")
    object SettingsScreen : Screens("profile/settings/")    // сделать
    object YarnStockScreen : Screens("profile/yarn/")
    object CreateYarnScreen : Screens("profile/yarn/create")
    object NeedleStockScreen : Screens("profile/needle/")
    object CreateNeedleScreen : Screens("profile/needle/create")
    object SubsScreen : Screens("profile/subs/")


    // Экраны проектов
    object ProjectsScreen : Screens("projects/")
    object ProjectScreen : Screens("projects/{projectId}/")
    object PartScreen : Screens("projects/{projectId}/{partId}/{count}/")
    object CreateProjectScreen : Screens("projects-create/")
    object CreatePartScreen : Screens("projects-create/{projectId}/")


    // Экраны соцсети
    object FeedScreen : Screens("social/")
    object ProfileScreen : Screens("social/{userId}/")

}