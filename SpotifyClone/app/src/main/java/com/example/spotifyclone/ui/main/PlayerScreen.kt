package com.example.spotifyclone.ui.main

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spotifyclone.viewmodel.PlayerViewModel
import java.util.concurrent.TimeUnit

@Composable
fun PlayerScreen(
    playerViewModel: PlayerViewModel,
    onBackClick: () -> Unit
) {
    val currentSong by playerViewModel.currentSong.collectAsState()
    val isPlaying by playerViewModel.isPlaying.collectAsState()
    val currentPos by playerViewModel.currentPosition.collectAsState()
    val totalDur by playerViewModel.totalDuration.collectAsState()

    // --- LOGIC XOAY ĐĨA ---
    val infiniteTransition = rememberInfiniteTransition(label = "VinylRotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RotationAngle"
    )

    val currentRotation = if (isPlaying) rotation else 0f
    val backgroundBrush = Brush.verticalGradient(colors = listOf(Color(0xFF4B4B4B), Color.Black))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(24.dp)
            .statusBarsPadding()
    ) {
        // 1. Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White, modifier = Modifier.size(32.dp))
            }
            Text("Đang phát từ danh sách", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreVert, null, tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // 2. ĐĨA NHẠC XOAY
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(20.dp)
                .graphicsLayer { rotationZ = currentRotation },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = currentSong?.coverUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier.size(45.dp).clip(CircleShape).background(Color.Black.copy(alpha = 0.6f)))
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF4B4B4B)))
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // 3. Thông tin bài hát
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(currentSong?.title ?: "", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(currentSong?.artistName ?: "Nghệ sĩ", color = Color.Gray, fontSize = 18.sp)
            }
            IconButton(onClick = {}) { Icon(Icons.Default.FavoriteBorder, null, tint = Color.White) }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. SeekBar (Slider)
        Slider(
            value = currentPos.toFloat(),
            onValueChange = { playerViewModel.seekTo(it.toLong()) },
            valueRange = 0f..(totalDur.toFloat().coerceAtLeast(1f)),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.Gray.copy(alpha = 0.3f)
            )
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(formatTime(currentPos), color = Color.Gray, fontSize = 12.sp)
            Text(formatTime(totalDur), color = Color.Gray, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 5. BỘ ĐIỀU KHIỂN XỊN XÒ
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) { Icon(Icons.Default.Shuffle, null, tint = Color(0xFF1DB954)) }

            IconButton(onClick = { playerViewModel.playPrevious() }) {
                Icon(Icons.Default.SkipPrevious, null, tint = Color.White, modifier = Modifier.size(40.dp))
            }

            IconButton(onClick = { playerViewModel.seekBackward() }) {
                Icon(Icons.Default.Replay10, null, tint = Color.White, modifier = Modifier.size(32.dp))
            }

            Surface(
                modifier = Modifier.size(72.dp).clip(CircleShape),
                color = Color.White,
                onClick = { playerViewModel.togglePlayPause() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null, tint = Color.Black, modifier = Modifier.size(44.dp))
                }
            }

            IconButton(onClick = { playerViewModel.seekForward() }) {
                Icon(Icons.Default.Forward10, null, tint = Color.White, modifier = Modifier.size(32.dp))
            }

            IconButton(onClick = { playerViewModel.playNext() }) {
                Icon(Icons.Default.SkipNext, null, tint = Color.White, modifier = Modifier.size(40.dp))
            }

            IconButton(onClick = {}) { Icon(Icons.Default.Repeat, null, tint = Color(0xFF1DB954)) }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

// HÀM FORMAT THỜI GIAN
fun formatTime(ms: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
    return String.format("%02d:%02d", minutes, seconds)
}