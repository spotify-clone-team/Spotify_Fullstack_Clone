package com.example.spotifyclone.data.model
// (Nhớ giữ nguyên dòng package cũ của bồ ở dòng 1 nha)

data class Artist(
    val _id: String,
    val name: String,
    val imageUrl: String? = null, // Thêm ? = null để chống crash nếu API thiếu
    val bio: String? = null
)

data class Album(
    val _id: String,
    val title: String,
    val artist: Artist? = null,
    val coverUrl: String? = null,
    val releaseYear: Int? = null
)

// ĐÂY LÀ NHÂN VẬT CHÍNH: Cập nhật List<String> và thêm dấu ?
data class Playlist(
    val _id: String,
    val title: String,
    val description: String? = null,
    val coverUrl: String? = null,
    val songs: List<Song>? = emptyList(), 
    val isPublic: Boolean? = true
)