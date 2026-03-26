package com.example.spotifyclone.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spotifyclone.ui.theme.SpotifyBlack
import com.example.spotifyclone.ui.theme.SpotifyCard
import com.example.spotifyclone.ui.theme.SpotifyTextSecondary
import com.example.spotifyclone.viewmodel.SongViewModel

@Composable
fun SearchScreen(
    viewModel: SongViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var query by remember { mutableStateOf("") }

    val filteredSongs = uiState.songs.filter {
        val keyword = query.trim()
        if (keyword.isBlank()) true
        else {
            it.title.contains(keyword, ignoreCase = true) ||
                    (it.artist?.name ?: it.artistName ?: "").contains(keyword, ignoreCase = true) ||
                    (it.album?.title ?: it.albumName ?: "").contains(keyword, ignoreCase = true) ||
                    (it.genre ?: "").contains(keyword, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotifyBlack)
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        Text(
            text = "Search",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Artists, songs, albums...") },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxSize(fraction = 1f)
        )

        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredSongs) { song ->
                Surface(
                    color = SpotifyCard,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = song.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = song.artist?.name ?: song.artistName ?: "Unknown artist",
                            color = SpotifyTextSecondary
                        )
                    }
                }
            }
        }
    }
}