package com.practicum.playlistmaker.presentation.presenters
/*
import android.media.MediaPlayer
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.ui.library.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class TrackMediaPlayer(val previewUrl : String) {
    var mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT

    // Функции для работы с плеером
    private fun preparePlayer() {

        if (!previewUrl.isNullOrEmpty()) {
            mediaPlayer.setDataSource(previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                play.isEnabled = true
                playerState = PlayerState.STATE_PREPARED
            }

            mediaPlayer.setOnCompletionListener {
                play.setImageResource(R.drawable.play_button_play)
                playerState = PlayerState.STATE_PREPARED
                handler.removeCallbacks(playingProgressRunnable)
                playingTrackTime.text = "00:00"
                mediaPlayer.seekTo(0)
            }

        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        play.setImageResource(R.drawable.play_button_pause)
        playerState = PlayerState.STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play.setImageResource(R.drawable.play_button_play)
        playerState = PlayerState.STATE_PAUSED
    }
    private fun playbackControl() {

        when(playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
                handler.removeCallbacks(playingProgressRunnable)
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
                handler.postDelayed(playingProgressRunnable, PLAYING_PROGRESS_DEBOUNCE_DELAY)
            }
        }
    }

    private fun displayTime(){
        if (playerState == PlayerState.STATE_PLAYING){
            val currentPosition = mediaPlayer.getCurrentPosition().toLong()
            playingTrackTime.text= SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
            handler.postDelayed(playingProgressRunnable, PLAYING_PROGRESS_DEBOUNCE_DELAY)
        }
    }
    companion object {

        private const val PLAYING_PROGRESS_DEBOUNCE_DELAY = 500L
    }
}

 */