package com.example.spotifyclone.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.spotifyclone.ui.home.HomeScreen
import com.example.spotifyclone.ui.library.LibraryScreen
import com.example.spotifyclone.ui.search.SearchScreen

sealed class BottomRoute(val route: String, val label: String) {
    data object Home : BottomRoute("home", "Home")
    data object Search : BottomRoute("search", "Search")
    data object Library : BottomRoute("library", "Library")
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val items = listOf(
        Triple(BottomRoute.Home.route, "Home", Icons.Outlined.Home),
        Triple(BottomRoute.Search.route, "Search", Icons.Outlined.Search),
        Triple(BottomRoute.Library.route, "Library", Icons.Outlined.LibraryMusic)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.first,
                        onClick = {
                            navController.navigate(item.first) {
                                popUpTo(BottomRoute.Home.route)
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.third,
                                contentDescription = item.second
                            )
                        },
                        label = {
                            Text(item.second)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomRoute.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomRoute.Home.route) { HomeScreen() }
            composable(BottomRoute.Search.route) { SearchScreen() }
            composable(BottomRoute.Library.route) { LibraryScreen() }
        }
    }
}