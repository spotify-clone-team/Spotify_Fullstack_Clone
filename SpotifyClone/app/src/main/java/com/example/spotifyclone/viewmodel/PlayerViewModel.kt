package com.example.spotifyclone.viewmodel

import android.app.Application
import android.content.ComponentName
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.spotifyclone.data.model.Song
import com.example.spotifyclone.service.PlaybackService
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    private var controllerFuture: ListenableFuture<MediaController>? = null
    var player: Player? = null

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    // --- THÊM MỚI: Dây nối cho thanh SeekBar (Thời gian) ---
    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _totalDuration = MutableStateFlow(0L)
    val totalDuration: StateFlow<Long> = _totalDuration.asStateFlow()

    private var progressJob: Job? = null // Biến đếm thời gian chạy

    init {
        val sessionToken = SessionToken(
            application,
            ComponentName(application, PlaybackService::class.java)
        )
        controllerFuture = MediaController.Builder(application, sessionToken).buildAsync()

        controllerFuture?.addListener({
            player = controllerFuture?.get()

            player?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isPlaying.value = isPlaying
                    // Nếu đang hát thì đếm thời gian, dừng thì ngừng đếm
                    if (isPlaying) startProgressTracker() else stopProgressTracker()
                }

                override fun onPlaybackStateChanged(state: Int) {
                    // Khi bài hát tải xong, lấy tổng thời gian bài hát
                    if (state == Player.STATE_READY) {
                        _totalDuration.value = player?.duration?.coerceAtLeast(0L) ?: 0L
                    }
                }
            })
        }, ContextCompat.getMainExecutor(application))
    }

    fun playSong(song: Song) {
        _currentSong.value = song
        player?.let {
            val metadata = MediaMetadata.Builder()
                .setTitle(song.title)
                .setArtist(song.artistName ?: "Unknown Artist")
                .setArtworkUri(Uri.parse(song.coverUrl ?: ""))
                .build()

            val mediaItem = MediaItem.Builder()
                .setUri(song.audioUrl)
                .setMediaMetadata(metadata)
                .build()

            it.setMediaItem(mediaItem)
            it.prepare()
            it.play()
        }
    }

    fun togglePlayPause() {
        player?.let { if (it.isPlaying) it.pause() else it.play() }
    }

    // --- THÊM MỚI: Các hàm điều khiển cho màn hình PlayerScreen ---

    fun seekTo(position: Long) {
        player?.seekTo(position)
        _currentPosition.value = position
    }

    fun seekForward() {
        player?.let { it.seekTo((it.currentPosition + 10000).coerceAtMost(it.duration)) }
    }

    fun seekBackward() {
        player?.let { it.seekTo((it.currentPosition - 10000).coerceAtLeast(0)) }
    }

    // Tạm thời thiết lập Next/Previous (Nếu bồ có mảng Playlist thì mình xử lý sâu hơn sau)
    fun playNext() {
        // Code nhảy bài tiếp theo (Cần có danh sách bài)
    }

    fun playPrevious() {
        // Code lùi bài trước đó
    }

    // --- THÊM MỚI: Đồng hồ chạy theo thời gian thực ---
    private fun startProgressTracker() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (isActive) {
                _currentPosition.value = player?.currentPosition?.coerceAtLeast(0L) ?: 0L
                delay(1000L) // Cứ 1 giây là cập nhật thanh SeekBar 1 lần
            }
        }
    }

    private fun stopProgressTracker() {
        progressJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        controllerFuture?.let { MediaController.releaseFuture(it) }
    }
}