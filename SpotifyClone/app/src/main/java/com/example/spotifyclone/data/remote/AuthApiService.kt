package com.example.spotifyclone.data.remote

import com.example.spotifyclone.data.model.ApiResponse
import com.example.spotifyclone.data.model.AuthData
import com.example.spotifyclone.data.model.LoginRequest
import com.example.spotifyclone.data.model.RegisterRequest
import com.example.spotifyclone.data.model.SignatureData
import com.example.spotifyclone.data.model.User
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): ApiResponse<AuthData>

    @POST("auth/register")
    suspend fun register(
        @Body body: RegisterRequest
    ): ApiResponse<AuthData>

    @GET("uploads/signature")
    suspend fun getSignature(
        @Header("Authorization") token: String,
        @Query("folder") folder: String = "spotify-clone/avatars"
    ): ApiResponse<SignatureData>

    @PUT("users/me/avatar-url")
    suspend fun updateAvatarUrl(
        @Header("Authorization") token: String,
        @Body body: Map<String, String>
    ): ApiResponse<User>

    @Multipart
    @PUT("users/me/avatar")
    suspend fun uploadAvatar(
        @Header("Authorization") authorization: String,
        @Part avatar: MultipartBody.Part
    ): ApiResponse<User>
}