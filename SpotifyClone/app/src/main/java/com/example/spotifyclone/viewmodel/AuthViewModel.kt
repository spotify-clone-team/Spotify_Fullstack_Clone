package com.example.spotifyclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyclone.data.model.AuthData
import com.example.spotifyclone.data.repository.AuthRepository
import com.example.spotifyclone.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val authData: AuthData? = null
)

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String, sessionManager: SessionManager) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val result = repository.login(email, password)
                result.token?.let { sessionManager.saveToken(it) }
                _uiState.update { it.copy(isLoading = false, authData = result) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = parseError(e)) }
            }
        }
    }

    fun register(name: String, email: String, password: String, sessionManager: SessionManager) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val result = repository.register(name, email, password)
                result.token?.let { sessionManager.saveToken(it) }
                _uiState.update { it.copy(isLoading = false, authData = result) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = parseError(e)) }
            }
        }
    }

    private fun parseError(e: Exception): String {
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    401 -> "Sai email hoặc mật khẩu rồi!"
                    409 -> "Email này đã được sử dụng mất rồi."
                    500 -> "Lỗi hệ thống, vui lòng thử lại sau."
                    else -> "Lỗi kết nối: ${e.code()}"
                }
            }
            else -> "Không thể kết nối đến máy chủ."
        }
    }

    fun clearError() { _uiState.update { it.copy(error = null) } }
}