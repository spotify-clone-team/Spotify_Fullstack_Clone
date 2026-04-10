package com.example.spotifyclone.ui.library

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    type: String,
    id: String,
    title: String,
    playerViewModel: PlayerViewModel,
    homeViewModel: HomeViewModel,
    libraryViewModel: LibraryViewModel,
    onBackClick: () -> Unit
) {
    val decodedTitle = Uri.decode(title)
    val homeUiState by homeViewModel.uiState.collectAsState()
    val libraryUiState by libraryViewModel.uiState.collectAsState()
    val allSongs = homeUiState.songs

    val filteredSongs: List<Song> = when (type) {
        "ALBUM" -> allSongs.filter { it.album?._id == id }
        "ARTIST" -> allSongs.filter { it.artist?._id == id }
        "PLAYLIST" -> {
            val currentPlaylist = libraryUiState.playlists.find { it._id == id }
            currentPlaylist?.songs ?: emptyList()
        }
        else -> emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Text(
                text = decodedTitle,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        when {
            homeUiState.isLoading || libraryUiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1DB954))
                }
            }

            filteredSongs.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Mục này chưa có bài hát nào.",
                        color = Color.Gray
                    )
                }
            }

            else -> {
                LazyColumn {
                    items(filteredSongs) { song ->
                        SongDetailRow(
                            song = song,
                            onClick = { playerViewModel.playSong(song) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SongDetailRow(
    song: Song,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = song.coverUrl,
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = song.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = song.artistName ?: song.artist?.name ?: "Unknown",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}