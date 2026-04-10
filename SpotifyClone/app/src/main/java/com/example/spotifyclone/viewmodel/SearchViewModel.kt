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

data class SearchUiState(
    val query: String = "",
    val allSongs: List<Song> = emptyList(),
    val filteredSongs: List<Song> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class SearchViewModel(
    private val repository: SongRepository = SongRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadAllSongs()
    }

    private fun loadAllSongs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val response = repository.getSongs()
                val songs = response.data ?: emptyList()

                _uiState.update {
                    it.copy(
                        allSongs = songs,
                        filteredSongs = songs,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Không thể tải dữ liệu tìm kiếm"
                    )
                }
            }
        }
    }

    fun onQueryChange(newQuery: String) {
        _uiState.update { state ->
            val filtered = if (newQuery.isBlank()) {
                state.allSongs
            } else {
                state.allSongs.filter { song ->
                    song.title.contains(newQuery, ignoreCase = true) ||
                    (song.artistName?.contains(newQuery, ignoreCase = true) ?: false) ||
                    (song.artist?.name?.contains(newQuery, ignoreCase = true) ?: false)
                }
            }

            state.copy(
                query = newQuery,
                filteredSongs = filtered
            )
        }
    }
}