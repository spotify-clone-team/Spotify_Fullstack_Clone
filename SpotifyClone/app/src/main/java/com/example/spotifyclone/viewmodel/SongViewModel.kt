package com.example.spotifyclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyclone.data.model.Song
import com.example.spotifyclone.data.repository.SongRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun fetchSongs() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val songs = repository.getSongs()
                _uiState.value = SongUiState(
                    isLoading = false,
                    songs = songs,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = SongUiState(
                    isLoading = false,
                    songs = emptyList(),
                    error = e.message ?: "Không thể tải dữ liệu"
                )
            }
        }
    }
}