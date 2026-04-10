package com.example.spotifyclone.data.repository

import com.example.spotifyclone.data.model.AuthData
import com.example.spotifyclone.data.model.LoginRequest
import com.example.spotifyclone.data.model.RegisterRequest
import com.example.spotifyclone.data.remote.ApiClient

class AuthRepository {

    suspend fun login(email: String, password: String): AuthData {
        val response = ApiClient.authApiService.login(
            LoginRequest(email = email, password = password)
        )

        return response.data
            ?: throw IllegalStateException("Đăng nhập thất bại: máy chủ không trả về dữ liệu người dùng.")
    }

    suspend fun register(name: String, email: String, password: String): AuthData {
        val response = ApiClient.authApiService.register(
            RegisterRequest(name = name, email = email, password = password)
        )

        return response.data
            ?: throw IllegalStateException("Đăng ký thất bại: máy chủ không trả về dữ liệu người dùng.")
    }
}