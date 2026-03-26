package com.example.spotifyclone.data.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val avatarUrl: String? = null
)

data class AuthData(
    val token: String,
    val user: User
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
