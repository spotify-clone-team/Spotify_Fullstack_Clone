package com.example.spotifyclone.data.repository

import android.content.Context
import com.example.spotifyclone.data.model.AuthData
import com.example.spotifyclone.data.model.LoginRequest
import com.example.spotifyclone.data.model.RegisterRequest
import com.example.spotifyclone.data.model.User
import com.example.spotifyclone.data.remote.ApiClient
import com.example.spotifyclone.utils.AuthStorage
import okhttp3.MultipartBody

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

    /**
     * 🔥 FIX 401: phải truyền Bearer token
     */
    suspend fun uploadAvatar(
        context: Context,
        avatarPart: MultipartBody.Part
    ): User {

        val token = AuthStorage.getToken(context)
            ?: throw IllegalStateException("Chưa đăng nhập, không có token")

        val response = ApiClient.authApiService.uploadAvatar(
            "Bearer $token",
            avatarPart
        )

        return response.data
            ?: throw IllegalStateException("Upload avatar thất bại: máy chủ không trả về dữ liệu người dùng.")
    }
}