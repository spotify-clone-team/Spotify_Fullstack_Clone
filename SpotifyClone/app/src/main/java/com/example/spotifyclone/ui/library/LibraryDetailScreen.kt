package com.example.spotifyclone.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spotifyclone.data.model.Song
import com.example.spotifyclone.viewmodel.HomeViewModel
import com.example.spotifyclone.viewmodel.LibraryViewModel
import com.example.spotifyclone.viewmodel.PlayerViewModel

@Composable
fun LibraryDetailScreen(
    type: String, // "ALBUM", "ARTIST", hoặc "PLAYLIST"
    id: String,   // _id của đối tượng bồ vừa nhấn
    title: String,
    playerViewModel: PlayerViewModel,
    homeViewModel: HomeViewModel,
    libraryViewModel: LibraryViewModel,
    onBackClick: () -> Unit
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    val libraryUiState by libraryViewModel.uiState.collectAsState()
    val allSongs = homeUiState.songs

    val filteredSongs: List<Song> = remember(allSongs, id, type) {
        when (type) {
            "ALBUM" -> allSongs.filter { it.album?._id == id }
            "ARTIST" -> allSongs.filter { it.artist?._id == id }
            "PLAYLIST" -> {
                val currentPlaylist = libraryUiState.playlists.find { it._id == id }
                // FIX Ở ĐÂY: Thêm <String> để báo cho Kotlin biết đây là danh sách rỗng chứa chữ
                val songIdsInPlaylist = currentPlaylist?.songs ?: emptyList<String>()
                allSongs.filter { song -> songIdsInPlaylist.contains(song._id) }
            }
            // FIX Ở ĐÂY: Thêm <Song> cho chắc ăn luôn
            else -> emptyList<Song>()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black).statusBarsPadding()) {
        // Top Bar
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }
            Text(title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        if (filteredSongs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Mục này chưa có bài hát nào đâu Dương!", color = Color.Gray)
            }
        } else {
            LazyColumn {
                items(filteredSongs) { song ->
                    SongDetailRow(song = song, onClick = { playerViewModel.playSong(song) })
                }
            }
        }
    }
}

@Composable
fun SongDetailRow(song: Song, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = song.coverUrl,
            contentDescription = null,
            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(song.title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(song.artistName ?: "Unknown", color = Color.Gray, fontSize = 14.sp)
        }
    }
}