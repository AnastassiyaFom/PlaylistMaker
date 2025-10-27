package com.practicum.playlistmaker.player.ui.viewModel

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel ( private val mediaPlayer: MediaPlayer
): ViewModel(){

    private var track:Track?=null
    private val checkedTrack = MutableLiveData<Track?>()
    fun observeCheckedTrack(): MutableLiveData<Track?> = checkedTrack

    private var playerStateLiveData:MutableLiveData<PlayerState>  = MutableLiveData( PlayerState.Default())
    fun observePlayerState(): MutableLiveData<PlayerState> = playerStateLiveData

    private var timerJob: Job? = null

    // Методы для трека
    fun loadTrack(trackToPlay: Track?):Track? {
        track = trackToPlay
        checkedTrack.postValue(track)
        preparePlayer()
        return track
    }

    // Методы для плеера
    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        releasePlayer()
    }
    fun onPlayButtonClicked() {
        when (playerStateLiveData.value) {
            is PlayerState.Playing -> pausePlayer()
            is PlayerState.Prepared, is  PlayerState.Paused -> startPlayer()
            else->{}
        }
    }
    fun onPause(){
        pausePlayer()
    }

    private fun preparePlayer() {
        val url:String =track?.previewUrl?:""
        if (url.isNotEmpty()) {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerStateLiveData.postValue(PlayerState.Prepared())
            }

            mediaPlayer.setOnCompletionListener {
                resetTimer()
                mediaPlayer.seekTo(0)
            }
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        playerStateLiveData.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimerUpdate()
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition) ?: "00:00"
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        playerStateLiveData.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    private fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        playerStateLiveData.value = PlayerState.Default()
        playerStateLiveData.postValue(PlayerState.Default())
    }

    private fun startTimerUpdate() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                 delay(PLAYING_PROGRESS_DEBOUNCE_DELAY)
                playerStateLiveData.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }

    private fun resetTimer() {
        timerJob?.cancel()
        playerStateLiveData.postValue(PlayerState.Prepared())
    }


    companion object {
        const val PLAYING_PROGRESS_DEBOUNCE_DELAY = 500L
    }
}
