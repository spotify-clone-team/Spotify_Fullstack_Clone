package com.example.spotifyclone.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.spotifyclone.data.model.Song
import com.example.spotifyclone.ui.theme.SpotifyBlack
import com.example.spotifyclone.viewmodel.HomeViewModel
import com.example.spotifyclone.viewmodel.PlayerViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    playerViewModel: PlayerViewModel // Nhận từ MainScreen
) {
    val uiState by homeViewModel.uiState.collectAsState()

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF3F3F3F), SpotifyBlack),
        startY = 0f,
        endY = 800f
    )

    Box(modifier = Modifier.fillMaxSize().background(gradientBrush)) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.Green)
        } else if (uiState.error != null) {
            Text(uiState.error!!, color = Color.Red, modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp).statusBarsPadding(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Chào buổi tối", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Row {
                            Icon(Icons.Default.History, null, tint = Color.White, modifier = Modifier.size(26.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(Icons.Default.Settings, null, tint = Color.White, modifier = Modifier.size(26.dp))
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        val quickSongs = uiState.songs.take(6).chunked(2)
                        quickSongs.forEach { pair ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                pair.forEach { song ->
                                    QuickAccessItem(song, Modifier.weight(1f)) { playerViewModel.playSong(song) }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                item {
                    Text("Mới phát gần đây", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(uiState.songs) { song ->
                            SongItemCard(song) { playerViewModel.playSong(song) }
                        }
                    }
                }

                item {
                    Text("Gợi ý cho bạn", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                }

                items(uiState.songs) { song ->
                    SongRowItem(song) { playerViewModel.playSong(song) }
                }
            }
        }
    }
}

@Composable
fun QuickAccessItem(song: Song, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.height(56.dp).clip(RoundedCornerShape(4.dp)).clickable { onClick() },
        color = Color.White.copy(alpha = 0.1f)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = song.coverUrl, contentDescription = null, modifier = Modifier.size(56.dp), contentScale = ContentScale.Crop)
            Text(song.title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp), maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun SongItemCard(song: Song, onClick: () -> Unit) {
    Column(modifier = Modifier.width(140.dp).clickable { onClick() }) {
        AsyncImage(model = song.coverUrl, contentDescription = null, modifier = Modifier.size(140.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
        Text(song.title, color = Color.White, maxLines = 1, modifier = Modifier.padding(top = 8.dp), fontWeight = FontWeight.SemiBold, overflow = TextOverflow.Ellipsis)
        Text(song.artistName ?: "Nghệ sĩ", color = Color.Gray, fontSize = 12.sp, maxLines = 1)
    }
}

@Composable
fun SongRowItem(song: Song, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(model = song.coverUrl, contentDescription = null, modifier = Modifier.size(56.dp).clip(RoundedCornerShape(4.dp)), contentScale = ContentScale.Crop)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(song.title, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp, maxLines = 1)
            Text(song.artistName ?: "Nghệ sĩ", color = Color.Gray, fontSize = 14.sp, maxLines = 1)
        }
    }
}