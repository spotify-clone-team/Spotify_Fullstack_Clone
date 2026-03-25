package com.example.spotifyclone.data.repository

import com.example.spotifyclone.data.model.Song
import com.example.spotifyclone.data.remote.ApiClient

class SongRepository {
    suspend fun getSongs(): List<Song> {
        return ApiClient.apiService.getSongs().data
    }
}