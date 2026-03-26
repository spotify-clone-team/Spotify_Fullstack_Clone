package com.example.spotifyclone.navigation

object AppRoute {
    // Luồng Xác thực
    const val WELCOME = "welcome"
    const val SIGN_UP_OPTIONS = "signup_options"
    const val LOGIN_OPTIONS = "login_options"
    const val REGISTER = "register"
    const val LOGIN = "login"

    // Luồng App Chính
    const val MAIN = "main" // Đây là vỏ bọc chứa BottomNav
    const val HOME = "home"
    const val SEARCH = "search"
    const val LIBRARY = "library"

    // Màn hình phát nhạc (FULL SCREEN)
    const val PLAYER = "player"
}