package com.example.spotifyclone.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spotifyclone.ui.theme.SpotifyBlack
import com.example.spotifyclone.utils.SessionManager
import com.example.spotifyclone.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    sessionManager: SessionManager,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState.authData) {
        if (uiState.authData != null) {
            onLoginSuccess()
        }
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            Toast.makeText(context, uiState.error, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotifyBlack)
            .padding(24.dp)
            .statusBarsPadding()
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.offset(x = (-12).dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Email và mật khẩu",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(24.dp))

        SpotifyInputField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            keyboardType = KeyboardType.Email
        )

        SpotifyInputField(
            value = password,
            onValueChange = { password = it },
            label = "Mật khẩu",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        SpotifyAuthButton(
            text = "Đăng nhập",
            isLoading = uiState.isLoading,
            onClick = {
                viewModel.login(email, password, sessionManager)
            }
        )
    }
}