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

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _totalDuration = MutableStateFlow(0L)
    val totalDuration: StateFlow<Long> = _totalDuration.asStateFlow()

    private var progressJob: Job? = null

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
                    if (isPlaying) {
                        startProgressTracker()
                    } else {
                        stopProgressTracker()
                    }
                }

                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_READY -> {
                            _totalDuration.value = player?.duration?.coerceAtLeast(0L) ?: 0L
                        }

                        Player.STATE_ENDED -> {
                            _isPlaying.value = false
                            stopProgressTracker()
                            _currentPosition.value = _totalDuration.value
                        }

                        Player.STATE_IDLE -> {
                            _isPlaying.value = false
                            stopProgressTracker()
                        }
                    }
                }
            })
        }, ContextCompat.getMainExecutor(application))
    }

    fun playSong(song: Song) {
        _currentSong.value = song
        _currentPosition.value = 0L
        _totalDuration.value = 0L

        player?.let { mediaPlayer ->
            val artistDisplayName = song.artistName ?: song.artist?.name ?: "Unknown Artist"

            val metadata = MediaMetadata.Builder()
                .setTitle(song.title)
                .setArtist(artistDisplayName)
                .setArtworkUri(
                    song.coverUrl?.takeIf { it.isNotBlank() }?.let { Uri.parse(it) }
                )
                .build()

            val mediaItem = MediaItem.Builder()
                .setUri(song.audioUrl)
                .setMediaMetadata(metadata)
                .build()

            mediaPlayer.setMediaItem(mediaItem)
            mediaPlayer.prepare()
            mediaPlayer.play()
        }
    }

    fun togglePlayPause() {
        player?.let { mediaPlayer ->
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            } else {
                mediaPlayer.play()
            }
        }
    }

    fun seekTo(position: Long) {
        player?.seekTo(position)
        _currentPosition.value = position
    }

    fun seekForward() {
        player?.let { mediaPlayer ->
            val duration = mediaPlayer.duration.takeIf { it > 0 } ?: Long.MAX_VALUE
            mediaPlayer.seekTo((mediaPlayer.currentPosition + 10000).coerceAtMost(duration))
        }
    }

    fun seekBackward() {
        player?.let { mediaPlayer ->
            mediaPlayer.seekTo((mediaPlayer.currentPosition - 10000).coerceAtLeast(0))
        }
    }

    fun playNext() {
        // Có thể xử lý sau khi app có queue/danh sách phát hiện tại
    }

    fun playPrevious() {
        // Có thể xử lý sau khi app có queue/danh sách phát hiện tại
    }

    private fun startProgressTracker() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (isActive) {
                _currentPosition.value = player?.currentPosition?.coerceAtLeast(0L) ?: 0L
                delay(1000L)
            }
        }
    }

    private fun stopProgressTracker() {
        progressJob?.cancel()
        progressJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopProgressTracker()
        controllerFuture?.let { MediaController.releaseFuture(it) }
    }
}