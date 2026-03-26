package com.example.spotifyclone.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import com.example.spotifyclone.data.model.Song
import com.example.spotifyclone.navigation.AppRoute
import com.example.spotifyclone.ui.home.HomeScreen
import com.example.spotifyclone.ui.search.SearchScreen
import com.example.spotifyclone.ui.library.LibraryScreen
import com.example.spotifyclone.viewmodel.PlayerViewModel
import com.example.spotifyclone.viewmodel.LibraryViewModel
import com.example.spotifyclone.utils.SessionManager

@Composable
fun MainScreen(
    rootNavController: NavHostController, // Để điều hướng ra ngoài (Welcome) khi Logout
    playerViewModel: PlayerViewModel = viewModel()
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val currentSong by playerViewModel.currentSong.collectAsState()
    val isPlaying by playerViewModel.isPlaying.collectAsState()
    var showFullPlayer by remember { mutableStateOf(false) }

    // Nếu showFullPlayer = true, đè màn hình Player lên trên cùng
    if (showFullPlayer) {
        PlayerScreen(
            playerViewModel = playerViewModel,
            onBackClick = { showFullPlayer = false }
        )
    } else {
        Scaffold(
            bottomBar = {
                Column {
                    // Chỉ hiện MiniPlayer nếu đang có bài hát được chọn
                    currentSong?.let { song ->
                        MiniPlayer(
                            song = song,
                            isPlaying = isPlaying,
                            onToggle = { playerViewModel.togglePlayPause() },
                            onClick = { showFullPlayer = true }
                        )
                    }
                    SpotifyBottomBar(navController)
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavHost(
                    navController = navController,
                    startDestination = AppRoute.HOME
                ) {
                    // 1. Màn hình Home
                    composable(AppRoute.HOME) {
                        HomeScreen(playerViewModel = playerViewModel)
                    }

                    // 2. Màn hình Tìm kiếm
                    composable(AppRoute.SEARCH) {
                        SearchScreen(playerViewModel = playerViewModel)
                    }

                    // 3. Màn hình Thư viện (Đã fix dấu ngoặc thừa)
                    composable(AppRoute.LIBRARY) {
                        val libraryViewModel: LibraryViewModel = viewModel(
                            factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                    return LibraryViewModel(sessionManager = sessionManager) as T
                                }
                            }
                        )
                        LibraryScreen(
                            libraryViewModel = libraryViewModel,
                            playerViewModel = playerViewModel,
                            onLogoutClick = {
                                // Điều hướng về màn hình Welcome (Root)
                                rootNavController.navigate(AppRoute.WELCOME) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MiniPlayer(song: Song, isPlaying: Boolean, onToggle: () -> Unit, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = Color(0xFF2E2E2E)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = song.coverUrl,
                contentDescription = null,
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(song.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(song.artistName ?: "Nghệ sĩ", color = Color.Gray, fontSize = 12.sp, maxLines = 1)
            }
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun SpotifyBottomBar(navController: NavHostController) {
    val items = listOf(
        Triple(AppRoute.HOME, "Trang chủ", Icons.Default.Home),
        Triple(AppRoute.SEARCH, "Tìm kiếm", Icons.Default.Search),
        Triple(AppRoute.LIBRARY, "Thư viện", Icons.Default.LibraryMusic)
    )
    NavigationBar(containerColor = Color.Black.copy(alpha = 0.95f)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { (route, label, icon) ->
            NavigationBarItem(
                icon = { Icon(icon, null) },
                label = { Text(label, fontSize = 10.sp) },
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.Transparent,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}