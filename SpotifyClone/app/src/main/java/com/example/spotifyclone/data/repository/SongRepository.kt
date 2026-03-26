package com.example.spotifyclone.data.repository

import com.example.spotifyclone.data.model.ApiResponse
import com.example.spotifyclone.data.model.Song
import com.example.spotifyclone.data.model.Artist
import com.example.spotifyclone.data.model.Album
import com.example.spotifyclone.data.model.Playlist
import com.example.spotifyclone.data.remote.ApiClient

class SongRepository {
    
    // 1. Lấy danh sách toàn bộ bài hát
    suspend fun getSongs(): ApiResponse<List<Song>> {
        return ApiClient.apiService.getSongs()
    }

    // 2. Lấy danh sách Nghệ sĩ (Dữ liệu cho tab Artists)
    suspend fun getArtists(): ApiResponse<List<Artist>> {
        return ApiClient.apiService.getArtists()
    }

    // 3. Lấy danh sách Album (Dữ liệu cho tab Albums)
    suspend fun getAlbums(): ApiResponse<List<Album>> {
        return ApiClient.apiService.getAlbums()
    }

    // 4. Lấy danh sách Playlist (Dữ liệu cho tab Playlists)
    suspend fun getPlaylists(): ApiResponse<List<Playlist>> {
        return ApiClient.apiService.getPlaylists()
    }
}