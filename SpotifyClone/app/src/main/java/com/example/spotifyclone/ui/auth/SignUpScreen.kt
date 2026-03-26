package com.example.spotifyclone.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spotifyclone.ui.theme.SpotifyBlack
import com.example.spotifyclone.viewmodel.AuthViewModel
import com.example.spotifyclone.utils.SessionManager

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    onSignUpSuccess: () -> Unit,
    sessionManager: SessionManager, // Thêm cái này
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState.authData != null) { onSignUpSuccess() }
        if (uiState.error != null) {
            Toast.makeText(context, uiState.error, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(SpotifyBlack).padding(24.dp).statusBarsPadding()) {
        IconButton(onClick = onBackClick, modifier = Modifier.offset(x = (-12).dp)) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Tạo tài khoản", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(24.dp))

        SpotifyInputField(value = name, onValueChange = { name = it }, label = "Tên hiển thị")
        SpotifyInputField(value = email, onValueChange = { email = it }, label = "Email", keyboardType = KeyboardType.Email)
        SpotifyInputField(value = password, onValueChange = { password = it }, label = "Mật khẩu", isPassword = true)

        Spacer(modifier = Modifier.height(32.dp))
        SpotifyAuthButton(
            text = "Tạo tài khoản",
            isLoading = uiState.isLoading,
            onClick = { viewModel.register(name, email, password, sessionManager) } // Truyền sessionManager vào đây
        )
    }
}