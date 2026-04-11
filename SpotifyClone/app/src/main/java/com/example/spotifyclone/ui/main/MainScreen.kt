package com.example.spotifyclone.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.spotifyclone.data.model.Song
import com.example.spotifyclone.navigation.AppRoute
import com.example.spotifyclone.ui.home.HomeScreen
import com.example.spotifyclone.ui.library.LibraryDetailScreen
import com.example.spotifyclone.ui.library.LibraryScreen
import com.example.spotifyclone.ui.profile.ProfileScreen
import com.example.spotifyclone.ui.search.SearchScreen
import com.example.spotifyclone.utils.SessionManager
import com.example.spotifyclone.viewmodel.HomeViewModel
import com.example.spotifyclone.viewmodel.LibraryViewModel
import com.example.spotifyclone.viewmodel.PlayerViewModel
import com.example.spotifyclone.viewmodel.SearchViewModel

@Composable
fun MainScreen(
    rootNavController: NavHostController,
    playerViewModel: PlayerViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val libraryViewModel: LibraryViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return LibraryViewModel(sessionManager = sessionManager) as T
            }
        }
    )

    val searchViewModel: SearchViewModel = viewModel()

    val currentSong by playerViewModel.currentSong.collectAsState()
    val isPlaying by playerViewModel.isPlaying.collectAsState()
    var showFullPlayer by remember { mutableStateOf(false) }

    if (showFullPlayer) {
        PlayerScreen(
            playerViewModel = playerViewModel,
            onBackClick = { showFullPlayer = false }
        )
    } else {
        Scaffold(
            bottomBar = {
                Column {
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
                    composable(AppRoute.HOME) {
                        HomeScreen(
                            homeViewModel = homeViewModel,
                            playerViewModel = playerViewModel
                        )
                    }

                    composable(AppRoute.SEARCH) {
                        SearchScreen(
                            searchViewModel = searchViewModel,
                            playerViewModel = playerViewModel
                        )
                    }

                    composable(AppRoute.LIBRARY) {
                        LibraryScreen(
                            libraryViewModel = libraryViewModel,
                            playerViewModel = playerViewModel,
                            navController = navController,
                            onLogoutClick = {
                                rootNavController.navigate(AppRoute.WELCOME) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(AppRoute.PROFILE) {
                        ProfileScreen(
                            sessionManager = sessionManager,
                            onBackClick = { navController.popBackStack() },
                            onLogoutSuccess = {
                                rootNavController.navigate(AppRoute.WELCOME) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(
                        route = AppRoute.LIBRARY_DETAIL,
                        arguments = listOf(
                            navArgument("type") { type = NavType.StringType },
                            navArgument("id") { type = NavType.StringType },
                            navArgument("title") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val type = backStackEntry.arguments?.getString("type") ?: ""
                        val id = backStackEntry.arguments?.getString("id") ?: ""
                        val title = backStackEntry.arguments?.getString("title") ?: ""

                        LibraryDetailScreen(
                            type = type,
                            id = id,
                            title = title,
                            playerViewModel = playerViewModel,
                            homeViewModel = homeViewModel,
                            libraryViewModel = libraryViewModel,
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MiniPlayer(
    song: Song,
    isPlaying: Boolean,
    onToggle: () -> Unit,
    onClick: () -> Unit
) {
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
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = song.title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = song.artistName ?: song.artist?.name ?: "Nghệ sĩ",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    maxLines = 1
                )
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
                icon = { Icon(icon, contentDescription = null) },
                label = { Text(label, fontSize = 10.sp) },
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
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