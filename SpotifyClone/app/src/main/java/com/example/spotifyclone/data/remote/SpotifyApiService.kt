package com.example.spotifyclone.data.remote

import com.example.spotifyclone.data.model.Album
import com.example.spotifyclone.data.model.ApiResponse
import com.example.spotifyclone.data.model.Artist
import com.example.spotifyclone.data.model.Playlist
import com.example.spotifyclone.data.model.Song
import retrofit2.http.GET

interface SpotifyApiService {

    @GET("songs")
    suspend fun getSongs(): ApiResponse<List<Song>>

    @GET("artists")
    suspend fun getArtists(): ApiResponse<List<Artist>>

    @GET("albums")
    suspend fun getAlbums(): ApiResponse<List<Album>>

    @GET("playlists")
    suspend fun getPlaylists(): ApiResponse<List<Playlist>>
}