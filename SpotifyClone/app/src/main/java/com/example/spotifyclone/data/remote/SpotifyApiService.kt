package com.example.spotifyclone.data.remote

import com.example.spotifyclone.data.model.* // IMPORT TẤT CẢ MODEL Ở ĐÂY
import retrofit2.http.GET

interface SpotifyApiService {
    @GET("songs")
    suspend fun getSongs(): ApiResponse<List<Song>>

    @GET("artists") // Nhớ khớp với Route ở Backend nhé
    suspend fun getArtists(): ApiResponse<List<Artist>>

    @GET("albums")
    suspend fun getAlbums(): ApiResponse<List<Album>>

    @GET("playlists")
    suspend fun getPlaylists(): ApiResponse<List<Playlist>>
}