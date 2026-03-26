package com.example.spotifyclone.data.model

// Model cho Artist (Nghệ sĩ)
data class Artist(
    val _id: String,
    val name: String,
    val imageUrl: String = "",
    val bio: String = ""
)

// Model cho Album
data class Album(
    val _id: String,
    val title: String,
    val artist: Artist? = null,
    val coverUrl: String = "",
    val releaseYear: Int? = null
)

// Model cho Playlist
data class Playlist(
    val _id: String,
    val title: String,
    val description: String = "",
    val coverUrl: String = "",
    val songs: List<Song> = emptyList()
)