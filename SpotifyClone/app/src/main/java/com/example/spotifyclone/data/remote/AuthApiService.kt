package com.example.spotifyclone.data.remote

import com.example.spotifyclone.data.model.ApiResponse
import com.example.spotifyclone.data.model.AuthData
import com.example.spotifyclone.data.model.LoginRequest
import com.example.spotifyclone.data.model.RegisterRequest
import com.example.spotifyclone.data.model.User
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): ApiResponse<AuthData>

    @POST("auth/register")
    suspend fun register(
        @Body body: RegisterRequest
    ): ApiResponse<AuthData>

    @Multipart
    @PUT("users/me/avatar")
    suspend fun uploadAvatar(
        @Part avatar: MultipartBody.Part
    ): ApiResponse<User>
}