package com.example.spotifyclone.ui.auth

import androidx.compose.runtime.Composable

@Composable
fun LoginOptionsScreen(onBackClick: () -> Unit, onEmailClick: () -> Unit, onSignUpClick: () -> Unit) {
    AuthOptionsTemplate(
        title = "Đăng nhập vào Spotify",
        onBackClick = onBackClick,
        onEmailClick = onEmailClick,
        bottomText = "Bạn chưa có tài khoản?",
        bottomActionText = "Đăng ký",
        onBottomActionClick = onSignUpClick,
        showFacebook = true
    )
}