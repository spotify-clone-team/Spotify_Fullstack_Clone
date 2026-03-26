package com.example.spotifyclone.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SpotifyColorScheme = darkColorScheme(
    primary = SpotifyGreen,
    background = SpotifyBlack,
    surface = SpotifyDarkGray,
    surfaceVariant = SpotifyCard,
    onPrimary = SpotifyBlack,
    onBackground = SpotifyTextPrimary,
    onSurface = SpotifyTextPrimary
)

@Composable
fun SpotifyCloneTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SpotifyColorScheme,
        typography = Typography,
        content = content
    )
}