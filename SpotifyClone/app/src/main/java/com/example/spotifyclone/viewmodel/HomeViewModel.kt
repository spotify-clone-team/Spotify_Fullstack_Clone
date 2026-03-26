package com.example.spotifyclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifyclone.data.model.Song
import com.example.spotifyclone.data.repository.SongRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val songs: List<Song> = emptyList(),
    val error: String? = null
)

class HomeViewModel(
    private val repository: SongRepository = SongRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchSongs()
    }

    fun fetchSongs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = repository.getSongs()
                // Giả sử ApiResponse của bạn có thuộc tính 'data' chứa List<Song>
                // Nếu backend của bạn trả về thẳng List<Song>, hãy bỏ .data đi nhé
                _uiState.update { it.copy(isLoading = false, songs = response.data ?: emptyList()) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Không thể lấy danh sách nhạc") }
            }
        }
    }
}