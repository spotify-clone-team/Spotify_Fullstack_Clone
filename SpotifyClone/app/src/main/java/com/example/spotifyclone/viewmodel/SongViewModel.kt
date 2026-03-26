package com.example.spotifyclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyclone.data.model.Song
import com.example.spotifyclone.data.repository.SongRepository
import com.example.spotifyclone.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SongUiState(
    val isLoading: Boolean = false,
    val songs: List<Song> = emptyList(),
    val error: String? = null
)

class SongViewModel(
    private val repository: SongRepository = SongRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SongUiState())
    val uiState: StateFlow<SongUiState> = _uiState.asStateFlow()

    init {
        fetchSongs()
    }

    private fun fixUrl(path: String?): String {
        if (path == null) return ""
        if (path.startsWith("http")) return path
        // Nếu backend trả về /uploads... thì nối thêm domain Render vào
        val cleanPath = if (path.startsWith("/")) path else "/$path"
        return "${Constants.BASE_URL}$cleanPath"
    }

    fun fetchSongs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = repository.getSongs()
                val fixedSongs = (response.data ?: emptyList()).map { song ->
                    song.copy(
                        coverUrl = fixUrl(song.coverUrl),
                        audioUrl = fixUrl(song.audioUrl)
                    )
                }
                _uiState.update { it.copy(isLoading = false, songs = fixedSongs) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Lỗi kết nối server") }
            }
        }
    }
}