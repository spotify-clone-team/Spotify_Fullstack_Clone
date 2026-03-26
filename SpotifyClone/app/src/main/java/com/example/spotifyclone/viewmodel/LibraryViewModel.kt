package com.example.spotifyclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyclone.data.model.Album
import com.example.spotifyclone.data.model.Artist
import com.example.spotifyclone.data.model.Playlist
import com.example.spotifyclone.data.model.Song
import com.example.spotifyclone.data.repository.SongRepository
import com.example.spotifyclone.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LibraryUiState(
    val playlists: List<Playlist> = emptyList(),
    val artists: List<Artist> = emptyList(),
    val albums: List<Album> = emptyList(),
    val selectedFilter: String = "Playlists", // Mặc định hiện Playlist
    val isLoading: Boolean = false
)

class LibraryViewModel(
    private val repository: SongRepository = SongRepository(),
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Khởi động phát là tải Playlist luôn
        loadDataByFilter("Playlists")
    }

    fun toggleFilter(filter: String) {
        if (_uiState.value.selectedFilter == filter) return
        
        _uiState.update { it.copy(selectedFilter = filter) }
        loadDataByFilter(filter)
    }

    private fun loadDataByFilter(filter: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                when (filter) {
                    "Playlists" -> {
                        val response = repository.getPlaylists()
                        _uiState.update { it.copy(playlists = response.data ?: emptyList()) }
                    }
                    "Artists" -> {
                        val response = repository.getArtists()
                        _uiState.update { it.copy(artists = response.data ?: emptyList()) }
                    }
                    "Albums" -> {
                        val  response = repository.getAlbums()
                        _uiState.update { it.copy(albums = response.data ?: emptyList()) }
                    }
                }
            } catch (e: Exception) {
                // Xử lý lỗi ở đây nếu cần
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearSession()
            onLogoutSuccess()
        }
    }
}