package com.example.spotifyclone.ui.auth

import androidx.compose.runtime.Composable

@Composable
fun SignUpOptionsScreen(onBackClick: () -> Unit, onEmailClick: () -> Unit, onLoginClick: () -> Unit) {
    AuthOptionsTemplate(
        title = "Đăng ký để bắt\nđầu nghe",
        onBackClick = onBackClick,
        onEmailClick = onEmailClick,
        bottomText = "Bạn đã có tài khoản?",
        bottomActionText = "Đăng nhập",
        onBottomActionClick = onLoginClick,
        showFacebook = false
    )
}