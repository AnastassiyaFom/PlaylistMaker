package com.practicum.playlistmaker.presentation.ui.library

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val url: String) : ViewModel() {

    private var playerStateLiveData = MutableLiveData(PlayerState.STATE_DEFAULT)
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val progressTimeLiveData = MutableLiveData("00:00")
    fun observeProgressTime(): LiveData<String> = progressTimeLiveData

    private val mediaPlayer = MediaPlayer()

    private val handler = Handler(Looper.getMainLooper())

    private val timerRunnable = Runnable {
        if (playerStateLiveData.value == PlayerState.STATE_PLAYING) {
            startTimerUpdate()
        }

    }

    init {
        preparePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }
    fun onPlayButtonClicked() {
        when (playerStateLiveData.value) {
            PlayerState.STATE_PLAYING -> pausePlayer()
            PlayerState.STATE_PREPARED,  PlayerState.STATE_PAUSED -> startPlayer()
            else->{}
        }
    }
    fun onPause(){
         pausePlayer()
    }

    private fun preparePlayer() {
        if (!url.isNullOrEmpty()) {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {

                playerStateLiveData.postValue(PlayerState.STATE_PREPARED)
            }

            mediaPlayer.setOnCompletionListener {
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
                // binding.playButton.setImageResource(R.drawable.play_button_play)
                playerStateLiveData.postValue(PlayerState.STATE_PREPARED)
                handler.removeCallbacks(timerRunnable)
                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
               // binding.playingTrackTime.text = "00:00"
                mediaPlayer.seekTo(0)
                resetTimer()
            }

        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        playerStateLiveData.postValue(PlayerState.STATE_PLAYING)
        startTimerUpdate()
    }

    private fun pausePlayer() {
        pauseTimer()
        mediaPlayer.pause()
        playerStateLiveData.postValue(PlayerState.STATE_PAUSED)
    }

    private fun playbackControl() {
        when (playerStateLiveData.value) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
                handler.removeCallbacks(timerRunnable)
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
                handler.postDelayed(timerRunnable, PLAYING_PROGRESS_DEBOUNCE_DELAY)
            }
            else->{}
        }
    }

    private fun startTimerUpdate() {
        progressTimeLiveData.postValue(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition))
        handler.postDelayed(timerRunnable, PLAYING_PROGRESS_DEBOUNCE_DELAY)
    }

    private fun resetTimer() {
        handler.removeCallbacks(timerRunnable)
        progressTimeLiveData.postValue( "00:00")
    }

    private fun pauseTimer() {
        handler.removeCallbacks(timerRunnable)
    }

    companion object {
        fun getFactory(trackUrl: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(trackUrl)
            }
        }
        private const val PLAYING_PROGRESS_DEBOUNCE_DELAY = 500L
    }

}