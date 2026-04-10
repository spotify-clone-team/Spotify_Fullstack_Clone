package com.example.spotifyclone.data.model

data class Artist(
    val _id: String,
    val name: String,
    val imageUrl: String? = null,
    val bio: String? = null
)

data class Album(
    val _id: String,
    val title: String,
    val artist: Artist? = null,
    val coverUrl: String? = null,
    val releaseYear: Int? = null
)

data class Playlist(
    val _id: String,
    val title: String,
    val description: String? = null,
    val coverUrl: String? = null,
    val songs: List<Song> = emptyList(),
    val isPublic: Boolean = true
)