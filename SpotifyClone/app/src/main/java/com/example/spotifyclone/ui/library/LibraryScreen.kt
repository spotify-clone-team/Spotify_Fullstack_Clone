package com.example.spotifyclone.ui.library

import android.net.Uri
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.spotifyclone.navigation.AppRoute
import com.example.spotifyclone.ui.theme.SpotifyBlack
import com.example.spotifyclone.utils.AuthStorage
import com.example.spotifyclone.viewmodel.LibraryViewModel
import com.example.spotifyclone.viewmodel.PlayerViewModel

@Composable
fun LibraryScreen(
    libraryViewModel: LibraryViewModel,
    playerViewModel: PlayerViewModel,
    navController: NavHostController,
    onLogoutClick: () -> Unit
) {
    val uiState by libraryViewModel.uiState.collectAsState()
    var isMenuExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val avatarUrl = AuthStorage.getAvatarUrl(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotifyBlack)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                AsyncImage(
                    model = avatarUrl ?: "https://i.pravatar.cc/150?u=spotify_user",
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { isMenuExpanded = true },
                    contentScale = ContentScale.Crop
                )

                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                    modifier = Modifier.background(Color(0xFF282828))
                ) {
                    DropdownMenuItem(
                        text = { Text("View Profile", color = Color.White) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        onClick = {
                            isMenuExpanded = false
                            navController.navigate(AppRoute.PROFILE)
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Logout", color = Color.White) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        onClick = {
                            isMenuExpanded = false
                            onLogoutClick()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Spotify Library",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Recently played",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Icon(
                imageVector = Icons.Default.GridView,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF1DB954))
            }
        } else if (uiState.error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error ?: "Không thể tải dữ liệu",
                    color = Color.Red
                )
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
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                Brush.linearGradient(
                                                    listOf(
                                                        Color(0xFF450AF5),
                                                        Color(0xFFC4EFD9)
                                                    )
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    }
                                },
                                isPinned = true,
                                onClick = {}
                            )
                        }

                        items(uiState.playlists) { playlist ->
                            val encodedTitle = Uri.encode(playlist.title)

                            LibraryItem(
                                title = playlist.title,
                                subtitle = "Playlist • ${playlist.description}",
                                imageUrl = playlist.coverUrl,
                                onClick = {
                                    navController.navigate(
                                        "library_detail/PLAYLIST/${playlist._id}/$encodedTitle"
                                    )
                                }
                            )
                        }
                    }

                    "Artists" -> {
                        items(uiState.artists) { artist ->
                            val encodedTitle = Uri.encode(artist.name)

                            LibraryItem(
                                title = artist.name,
                                subtitle = "Artist",
                                imageUrl = artist.imageUrl,
                                isCircularImage = true,
                                onClick = {
                                    navController.navigate(
                                        "library_detail/ARTIST/${artist._id}/$encodedTitle"
                                    )
                                }
                            )
                        }
                    }

                    "Albums" -> {
                        items(uiState.albums) { album ->
                            val encodedTitle = Uri.encode(album.title)

                            LibraryItem(
                                title = album.title,
                                subtitle = "Album • ${album.artist?.name ?: "Unknown"}",
                                imageUrl = album.coverUrl,
                                onClick = {
                                    navController.navigate(
                                        "library_detail/ALBUM/${album._id}/$encodedTitle"
                                    )
                                }
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
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            leadingIcon()
        } else {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(if (isCircularImage) CircleShape else RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isPinned) {
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = null,
                        tint = Color(0xFF1ED760),
                        modifier = Modifier
                            .size(12.dp)
                            .rotate(45f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }
    }
}