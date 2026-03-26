package com.example.spotifyclone.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spotifyclone.data.model.Album
import com.example.spotifyclone.data.model.Artist
import com.example.spotifyclone.data.model.Playlist
import com.example.spotifyclone.ui.theme.SpotifyBlack
import com.example.spotifyclone.viewmodel.LibraryViewModel
import com.example.spotifyclone.viewmodel.PlayerViewModel

@Composable
fun LibraryScreen(
    libraryViewModel: LibraryViewModel,
    playerViewModel: PlayerViewModel,
    onLogoutClick: () -> Unit
) {
    val uiState by libraryViewModel.uiState.collectAsState()
    var isMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotifyBlack)
            .statusBarsPadding()
    ) {
        // --- 1. HEADER ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                AsyncImage(
                    model = "https://i.pravatar.cc/150?u=spotify_user",
                    contentDescription = null,
                    modifier = Modifier.size(36.dp).clip(CircleShape).clickable { isMenuExpanded = true },
                    contentScale = ContentScale.Crop
                )
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                    modifier = Modifier.background(Color(0xFF282828))
                ) {
                    DropdownMenuItem(
                        text = { Text("Đăng xuất", color = Color.White) },
                        leadingIcon = { Icon(Icons.Default.ExitToApp, null, tint = Color.White) },
                        onClick = { isMenuExpanded = false; onLogoutClick() }
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Your Library", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(28.dp))
        }

        // --- 2. FILTER CHIPS ---
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val chips = listOf("Playlists", "Artists", "Albums")
            items(chips) { chip ->
                val isSelected = uiState.selectedFilter == chip
                Surface(
                    modifier = Modifier.clickable { libraryViewModel.toggleFilter(chip) },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) Color(0xFF1DB954) else Color.White.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = chip,
                        color = if (isSelected) Color.Black else Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // --- 3. SORT BAR ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Sort, null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Recently played", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.Default.GridView, null, tint = Color.White, modifier = Modifier.size(20.dp))
        }

        // --- 4. DANH SÁCH DỮ LIỆU THỰC TẾ ---
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF1DB954))
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                when (uiState.selectedFilter) {
                    "Playlists" -> {
                        item {
                            LibraryItem(
                                title = "Liked Songs",
                                subtitle = "Playlist • Pinned",
                                leadingIcon = {
                                    Box(
                                        modifier = Modifier.size(64.dp).clip(RoundedCornerShape(4.dp))
                                            .background(Brush.linearGradient(listOf(Color(0xFF450af5), Color(0xFFc4efd9)))),
                                        contentAlignment = Alignment.Center
                                    ) { Icon(Icons.Default.Favorite, null, tint = Color.White) }
                                },
                                isPinned = true
                            )
                        }
                        items(uiState.playlists) { playlist ->
                            LibraryItem(
                                title = playlist.title,
                                subtitle = "Playlist • ${playlist.description}",
                                imageUrl = playlist.coverUrl
                            )
                        }
                    }
                    "Artists" -> {
                        items(uiState.artists) { artist ->
                            LibraryItem(
                                title = artist.name,
                                subtitle = "Artist",
                                imageUrl = artist.imageUrl,
                                isCircularImage = true // NGHỆ SĨ THÌ HÌNH TRÒN
                            )
                        }
                    }
                    "Albums" -> {
                        items(uiState.albums) { album ->
                            LibraryItem(
                                title = album.title,
                                subtitle = "Album • ${album.artist?.name ?: "Unknown"}",
                                imageUrl = album.coverUrl
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LibraryItem(
    title: String,
    subtitle: String,
    imageUrl: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    isPinned: Boolean = false,
    isCircularImage: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) leadingIcon()
        else {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.size(64.dp).clip(if (isCircularImage) CircleShape else RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isPinned) {
                    Icon(Icons.Default.PushPin, null, tint = Color(0xFF1ED760), modifier = Modifier.size(12.dp).rotate(45f))
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(subtitle, color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}