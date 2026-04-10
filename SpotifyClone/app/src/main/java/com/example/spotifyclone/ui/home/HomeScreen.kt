package com.example.spotifyclone.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import coil.compose.AsyncImage
import com.example.spotifyclone.data.model.Song
import com.example.spotifyclone.ui.theme.SpotifyBlack
import com.example.spotifyclone.viewmodel.HomeViewModel
import com.example.spotifyclone.viewmodel.PlayerViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    playerViewModel: PlayerViewModel
) {
    val uiState by homeViewModel.uiState.collectAsState()

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF3F3F3F), SpotifyBlack),
        startY = 0f,
        endY = 800f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Green
                )
            }

            uiState.error != null -> {
                Text(
                    text = uiState.error ?: "Đã có lỗi xảy ra",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .statusBarsPadding(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Chào buổi tối",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Row {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }

                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            val quickSongs = uiState.songs.take(6).chunked(2)

                            quickSongs.forEach { pair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    pair.forEach { song ->
                                        QuickAccessItem(
                                            song = song,
                                            modifier = Modifier.weight(1f),
                                            onClick = { playerViewModel.playSong(song) }
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Mới phát gần đây",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(uiState.songs) { song ->
                                SongItemCard(
                                    song = song,
                                    onClick = { playerViewModel.playSong(song) }
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Gợi ý cho bạn",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    items(uiState.songs) { song ->
                        SongRowItem(
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
fun QuickAccessItem(
    song: Song,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() },
        color = Color.White.copy(alpha = 0.1f)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = song.coverUrl,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = song.title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun SongItemCard(
    song: Song,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = song.coverUrl,
            contentDescription = null,
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = song.title,
            color = Color.White,
            maxLines = 1,
            modifier = Modifier.padding(top = 8.dp),
            fontWeight = FontWeight.SemiBold,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = song.artistName ?: "Nghệ sĩ",
            color = Color.Gray,
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}

@Composable
fun SongRowItem(
    song: Song,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
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

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = song.title,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                maxLines = 1
            )

            Text(
                text = song.artistName ?: "Nghệ sĩ",
                color = Color.Gray,
                fontSize = 14.sp,
                maxLines = 1
            )
        }
    }
}