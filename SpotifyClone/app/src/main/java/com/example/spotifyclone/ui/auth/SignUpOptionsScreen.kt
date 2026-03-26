package com.example.spotifyclone.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.spotifyclone.ui.theme.SpotifyBlack
import com.example.spotifyclone.ui.theme.SpotifyBorder
import com.example.spotifyclone.ui.theme.SpotifyGreen

@Composable
fun SignUpOptionsScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotifyBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.GraphicEq,
                contentDescription = null,
                tint = Color.White
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(
                text = "Đăng ký để bắt đầu nghe",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(top = 28.dp))

            AuthOptionButton(
                text = "Tiếp tục bằng email",
                icon = { Icon(Icons.Default.Email, null, tint = Color.Black) },
                containerColor = SpotifyGreen,
                contentColor = Color.Black,
                border = false,
                onClick = onContinueClick
            )

            Spacer(modifier = Modifier.padding(top = 12.dp))

            AuthOptionButton(
                text = "Tiếp tục bằng số điện thoại",
                icon = { Icon(Icons.Default.PhoneAndroid, null, tint = Color.White) },
                onClick = onContinueClick
            )

            Spacer(modifier = Modifier.padding(top = 12.dp))

            AuthOptionButton(
                text = "Tiếp tục bằng Google",
                leadingText = "G",
                onClick = onContinueClick
            )

            Spacer(modifier = Modifier.padding(top = 12.dp))

            AuthOptionButton(
                text = "Tiếp tục bằng Apple",
                leadingText = "",
                onClick = onContinueClick
            )

            Spacer(modifier = Modifier.padding(top = 36.dp))

            Text(
                text = "Bạn đã có tài khoản?",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = "Đăng nhập",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.Transparent)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
private fun AuthOptionButton(
    text: String,
    icon: @Composable (() -> Unit)? = null,
    leadingText: String? = null,
    containerColor: Color = Color.Transparent,
    contentColor: Color = Color.White,
    border: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(40.dp),
        border = if (border) {
            OutlinedButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(SpotifyBorder)
            )
        } else null,
        colors = OutlinedButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (icon != null) {
                Box(modifier = Modifier.align(Alignment.CenterStart)) {
                    icon()
                }
            } else if (leadingText != null) {
                Text(
                    text = leadingText,
                    modifier = Modifier.align(Alignment.CenterStart),
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
            }

            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 6.dp)
            )
        }
    }
}