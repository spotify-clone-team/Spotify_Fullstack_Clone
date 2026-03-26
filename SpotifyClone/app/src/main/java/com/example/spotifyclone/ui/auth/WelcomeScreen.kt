package com.example.spotifyclone.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotifyclone.ui.theme.SpotifyBlack

@Composable
fun WelcomeScreen(onSignUpClick: () -> Unit, onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(SpotifyBlack).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
            Icon(painterResource(android.R.drawable.ic_media_play), contentDescription = null, tint = Color.Black, modifier = Modifier.size(36.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Hàng triệu bài hát.\nMiễn phí trên Spotify.",
            color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center, lineHeight = 38.sp
        )
        Spacer(modifier = Modifier.height(48.dp))

        SpotifyAuthButton(text = "Đăng ký miễn phí", onClick = onSignUpClick)
        Spacer(modifier = Modifier.height(16.dp))
        SpotifyAuthButton(text = "Đăng nhập", onClick = onLoginClick, isOutlined = true)
    }
}