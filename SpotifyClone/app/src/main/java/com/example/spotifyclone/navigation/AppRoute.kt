package com.example.spotifyclone.navigation

sealed class AppRoute(val route: String) {
    data object Welcome : AppRoute("welcome")
    data object Login : AppRoute("login")
    data object SignUp : AppRoute("signup")
    data object Main : AppRoute("main")
}