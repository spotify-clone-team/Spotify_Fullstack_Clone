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

    suspend fun getSignature(context: Context): com.example.spotifyclone.data.model.SignatureData {
        val token = AuthStorage.getToken(context)
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?: throw IllegalStateException("Chưa đăng nhập, không có token")

        val response = ApiClient.authApiService.getSignature("Bearer $token")
        return response.data
            ?: throw IllegalStateException("Lấy signature thất bại")
    }

    suspend fun updateAvatarUrl(context: Context, avatarUrl: String): User {
        val token = AuthStorage.getToken(context)
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?: throw IllegalStateException("Chưa đăng nhập, không có token")

        val response = ApiClient.authApiService.updateAvatarUrl(
            "Bearer $token",
            mapOf("avatarUrl" to avatarUrl)
        )
        return response.data
            ?: throw IllegalStateException("Cập nhật avatarURL thất bại")
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