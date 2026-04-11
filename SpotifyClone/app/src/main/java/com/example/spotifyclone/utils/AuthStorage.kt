package com.example.spotifyclone.utils

import android.content.Context

object AuthStorage {
    private const val PREF_NAME = "spotify_clone_prefs"
    private const val KEY_TOKEN = "token"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_ROLE = "user_role"
    private const val KEY_USER_AVATAR_URL = "user_avatar_url"

    fun saveAuth(
        context: Context,
        token: String,
        userName: String,
        userEmail: String,
        userRole: String,
        avatarUrl: String?
    ) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_USER_NAME, userName)
            .putString(KEY_USER_EMAIL, userEmail)
            .putString(KEY_USER_ROLE, userRole)
            .putString(KEY_USER_AVATAR_URL, avatarUrl)
            .apply()
    }

    /**
     * 🔥 THÊM MỚI: cập nhật avatar mà KHÔNG đụng tới token
     */
    fun updateAvatar(context: Context, avatarUrl: String?) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_USER_AVATAR_URL, avatarUrl)
            .apply()
    }

    fun getToken(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)
    }

    fun getUserName(context: Context): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER_NAME, "Spotify User") ?: "Spotify User"
    }

    fun getUserEmail(context: Context): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER_EMAIL, "unknown@email.com") ?: "unknown@email.com"
    }

    fun getUserRole(context: Context): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER_ROLE, "user") ?: "user"
    }

    fun getAvatarUrl(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USER_AVATAR_URL, null)
    }

    fun clearAuth(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}