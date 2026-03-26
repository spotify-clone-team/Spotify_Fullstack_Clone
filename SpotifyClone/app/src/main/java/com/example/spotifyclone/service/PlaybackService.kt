package com.example.spotifyclone.service

import android.content.Intent
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    // Hàm này chạy khi Service được khởi tạo
    override fun onCreate() {
        super.onCreate()

        // Khởi tạo máy phát nhạc ExoPlayer
        val player = ExoPlayer.Builder(this).build()

        // Tạo MediaSession để kết nối Player với Thanh thông báo (Notification)
        mediaSession = MediaSession.Builder(this, player).build()
    }

    // Trả về cái Session để Android biết đường vẽ cái thanh thông báo
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    // Hàm này chạy khi Service bị hủy (tắt app hoàn toàn)
    override fun onDestroy() {
        mediaSession?.run {
            player.release() // Giải phóng máy phát nhạc
            release() // Giải phóng Session
        }
        mediaSession = null
        super.onDestroy()
    }
}