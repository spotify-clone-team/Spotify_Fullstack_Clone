package com.example.spotifyclone.data.remote

import com.example.spotifyclone.data.model.ApiResponse
import com.example.spotifyclone.data.model.AuthData
import com.example.spotifyclone.data.model.LoginRequest
import com.example.spotifyclone.data.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): ApiResponse<AuthData>

    @POST("auth/register")
    suspend fun register(
        @Body body: RegisterRequest
    ): ApiResponse<AuthData>
}