package com.example.spotifyclone.data.remote

import com.example.spotifyclone.data.model.ApiResponse
import com.example.spotifyclone.data.model.Song
import retrofit2.http.GET

interface SpotifyApiService {
    @GET("songs")
    suspend fun getSongs(): ApiResponse<List<Song>>
}