package com.example.spotifyclone.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.spotifyclone.ui.home.HomeScreen
import com.example.spotifyclone.ui.library.LibraryScreen
import com.example.spotifyclone.ui.search.SearchScreen
import com.example.spotifyclone.ui.theme.SpotifyBlack
import com.example.spotifyclone.ui.theme.SpotifyCloneTheme

sealed class BottomRoute(
    val title: String
) {
    data object Home : BottomRoute("Home")
    data object Search : BottomRoute("Search")
    data object Library : BottomRoute("Library")
}

@Composable
fun MainScreen() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    val items = listOf(
        BottomRoute.Home,
        BottomRoute.Search,
        BottomRoute.Library
    )

    SpotifyCloneTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SpotifyBlack)
        ) {
            when (selectedIndex) {
                0 -> HomeScreen()
                1 -> SearchScreen()
                2 -> LibraryScreen()
            }

            NavigationBar(
                modifier = Modifier.navigationBarsPadding(),
                containerColor = SpotifyBlack
            ) {
                items.forEachIndexed { index, item ->
                    val icon = when (item) {
                        BottomRoute.Home -> Icons.Default.Home
                        BottomRoute.Search -> Icons.Default.Search
                        BottomRoute.Library -> Icons.Default.LibraryMusic
                    }

                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    }
}