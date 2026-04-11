package com.example.spotifyclone.navigation

object AppRoute {
    // Luồng Xác thực
    const val WELCOME = "welcome"
    const val SIGN_UP_OPTIONS = "signup_options"
    const val LOGIN_OPTIONS = "login_options"
    const val REGISTER = "register"
    const val LOGIN = "login"

    // Luồng App Chính
    const val MAIN = "main"
    const val HOME = "home"
    const val SEARCH = "search"
    const val LIBRARY = "library"
    const val PROFILE = "profile"

    // Màn hình phát nhạc
    const val PLAYER = "player"

    // Route chi tiết thư viện
    const val LIBRARY_DETAIL = "library_detail/{type}/{id}/{title}"
}