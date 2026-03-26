package com.example.spotifyclone.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotifyclone.ui.theme.SpotifyBlack
import com.example.spotifyclone.ui.theme.SpotifyBorder
import com.example.spotifyclone.ui.theme.SpotifyGreen

@Composable
fun SpotifyAuthButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    leadingText: String? = null,
    isOutlined: Boolean = false,
    containerColor: Color = if (isOutlined) Color.Transparent else SpotifyGreen,
    contentColor: Color = if (isOutlined) Color.White else Color.Black,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f)
        ),
        border = if (isOutlined) BorderStroke(1.dp, SpotifyBorder) else null,
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = contentColor, modifier = Modifier.size(24.dp))
        } else {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (icon != null) { Box(modifier = Modifier.align(Alignment.CenterStart)) { icon() } }
                else if (leadingText != null) { Text(text = leadingText, modifier = Modifier.align(Alignment.CenterStart), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = contentColor) }
                Text(text = text, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifyInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White.copy(alpha = 0.7f)) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, autoCorrect = true),
        // ĐÃ SỬA LỖI Ở ĐOẠN NÀY: Dùng OutlinedTextFieldDefaults thay vì TextFieldDefaults cũ
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color(0xFF2A2A2A),   // Đổi từ containerColor
            unfocusedContainerColor = Color(0xFF2A2A2A), // Đổi từ containerColor
            cursorColor = SpotifyGreen,
            focusedBorderColor = SpotifyGreen,
            unfocusedBorderColor = Color.Transparent,
        )
    )
}
@Composable
fun AuthOptionsTemplate(
    title: String, onBackClick: () -> Unit, onEmailClick: () -> Unit,
    bottomText: String, bottomActionText: String, onBottomActionClick: () -> Unit,
    showFacebook: Boolean
) {
    Column(modifier = Modifier.fillMaxSize().background(SpotifyBlack).padding(24.dp).statusBarsPadding()) {
        IconButton(onClick = onBackClick, modifier = Modifier.offset(x = (-12).dp)) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
        }
        Column(modifier = Modifier.fillMaxWidth().weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                Icon(painterResource(android.R.drawable.ic_media_play), null, tint = Color.Black)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = title, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(32.dp))

            SpotifyAuthButton(text = "Tiếp tục bằng email", onClick = onEmailClick, icon = { Icon(Icons.Default.Email, null, tint = Color.Black) })
            Spacer(modifier = Modifier.height(12.dp))
            SpotifyAuthButton(text = "Tiếp tục bằng số điện thoại", onClick = {}, isOutlined = true, icon = { Icon(Icons.Default.PhoneAndroid, null, tint = Color.White) })
            Spacer(modifier = Modifier.height(12.dp))
            SpotifyAuthButton(text = "Tiếp tục bằng Google", onClick = {}, isOutlined = true, leadingText = "G")
            if (showFacebook) {
                Spacer(modifier = Modifier.height(12.dp))
                SpotifyAuthButton(text = "Tiếp tục bằng Facebook", onClick = {}, isOutlined = true, leadingText = "f")
            }
            Spacer(modifier = Modifier.height(12.dp))
            SpotifyAuthButton(text = "Tiếp tục bằng Apple", onClick = {}, isOutlined = true, leadingText = "")
        }
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = bottomText, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = bottomActionText, color = Color.White, fontSize = 16.sp, modifier = Modifier.clickable { onBottomActionClick() }.padding(8.dp))
        }
    }
}