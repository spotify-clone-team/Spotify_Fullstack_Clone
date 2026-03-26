package com.example.spotifyclone.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.spotifyclone.data.model.Song
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlayerViewModel(application: Application) : AndroidViewModel(application) {
    private val exoPlayer = ExoPlayer.Builder(application).build()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition = _currentPosition.asStateFlow()

    private val _totalDuration = MutableStateFlow(0L)
    val totalDuration = _totalDuration.asStateFlow()

    // Danh sách bài hát để chuyển bài (Next/Prev)
    private var playlist: List<Song> = emptyList()
    private var currentIndex = -1

    init {
        // Lắng nghe sự kiện từ ExoPlayer để tự động chuyển bài khi hết nhạc
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    _totalDuration.value = exoPlayer.duration.coerceAtLeast(0)
                } else if (state == Player.STATE_ENDED) {
                    playNext() // Hết bài tự nhảy bài tiếp theo
                }
            }
        })
    }

    fun setPlaylist(list: List<Song>, startIndex: Int) {
        playlist = list
        currentIndex = startIndex
        playSong(playlist[currentIndex])
    }

    fun playSong(song: Song) {
        if (_currentSong.value?._id == song._id) {
            togglePlayPause()
            return
        }

        _currentSong.value = song
        val mediaItem = MediaItem.fromUri(song.audioUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()

        startProgressUpdates()
    }

    fun togglePlayPause() {
        if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
    }

    // --- CÁC HÀM XỊN XÒ BỒ CẦN ---

    fun seekForward() {
        val target = exoPlayer.currentPosition + 10000 // +10s
        exoPlayer.seekTo(target.coerceAtMost(exoPlayer.duration))
    }

    fun seekBackward() {
        val target = exoPlayer.currentPosition - 10000 // -10s
        exoPlayer.seekTo(target.coerceAtLeast(0))
    }

    fun playNext() {
        if (playlist.isNotEmpty() && currentIndex < playlist.size - 1) {
            currentIndex++
            playSong(playlist[currentIndex])
        }
    }

    fun playPrevious() {
        if (playlist.isNotEmpty() && currentIndex > 0) {
            currentIndex--
            playSong(playlist[currentIndex])
        }
    }

    fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
    }

    private fun startProgressUpdates() {
        CoroutineScope(Dispatchers.Main).launch {
            while (isActive && _currentSong.value != null) {
                _currentPosition.value = exoPlayer.currentPosition
                delay(500) // Cập nhật nhanh hơn (0.5s) cho mượt
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}