package com.example.spotifyclone.data.model

data class Song(
    val _id: String,
    val title: String,
    val artistName: String? = null,
    val albumName: String? = null,
    val genre: String? = null,
    val durationSeconds: Int? = null,
    val coverUrl: String? = null,
    val audioUrl: String,
    val artist: ArtistRef? = null,
    val album: AlbumRef? = null
)

data class ArtistRef(
    val _id: String,
    val name: String
)

data class AlbumRef(
    val _id: String,
    val title: String
)