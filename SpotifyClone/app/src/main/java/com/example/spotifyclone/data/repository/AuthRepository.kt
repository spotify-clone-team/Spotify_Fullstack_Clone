package com.example.spotifyclone.data.repository

import android.content.Context
import android.util.Log
import com.example.spotifyclone.data.model.AuthData
import com.example.spotifyclone.data.model.LoginRequest
import com.example.spotifyclone.data.model.RegisterRequest
import com.example.spotifyclone.data.model.User
import com.example.spotifyclone.data.remote.ApiClient
import com.example.spotifyclone.utils.AuthStorage
import okhttp3.MultipartBody

class AuthRepository {

    companion object {
        private const val TAG = "AuthRepository"
    }

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

    suspend fun uploadAvatar(
        context: Context,
        avatarPart: MultipartBody.Part
    ): User {
        val rawToken = AuthStorage.getToken(context)
        Log.d(TAG, "rawToken from AuthStorage = $rawToken")

        val token = rawToken
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?: throw IllegalStateException("Chưa đăng nhập, không có token")

        val authorization = "Bearer $token"
        Log.d(TAG, "authorization header = $authorization")

        val response = ApiClient.authApiService.uploadAvatar(
            authorization,
            avatarPart
        )

        return response.data
            ?: throw IllegalStateException("Upload avatar thất bại: máy chủ không trả về dữ liệu người dùng.")
    }
}