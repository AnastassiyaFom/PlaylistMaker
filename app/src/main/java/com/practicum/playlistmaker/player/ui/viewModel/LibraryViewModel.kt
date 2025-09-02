package com.practicum.playlistmaker.player.ui.viewModel

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.App
import com.practicum.playlistmaker.creator.Creator.provideLastCheckedTrackInteractor
import com.practicum.playlistmaker.search.domain.Track

import java.text.SimpleDateFormat
import java.util.Locale


class LibraryViewModel (private val context: Context,
    private var track:Track?): ViewModel(){
    private var  lastCheckedTrackInteractor= provideLastCheckedTrackInteractor(context)
    private val mediaPlayer = MediaPlayer()

    init{
        if (track==null) track=loadTrack()
        preparePlayer()
    }
    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable = Runnable {
        if (playerStateLiveData.value == PlayerState.STATE_PLAYING) {
            startTimerUpdate()
        }
    }

    private var playerStateLiveData = MutableLiveData(PlayerState.STATE_DEFAULT)
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val progressTimeLiveData = MutableLiveData("00:00")
    fun observeProgressTime(): LiveData<String> = progressTimeLiveData

    private val checkedTrack = MutableLiveData<Track?>()
    fun observeCheckedTrack(): MutableLiveData<Track?> = checkedTrack

    // Методы для трека

    fun loadTrack():Track? {
        // Получаем данные о треке, если поиска еще не было в текущей сессии
        if (track == null) {
            track = lastCheckedTrackInteractor.getLastCheckedTrack()
        }
        checkedTrack.postValue(track)
        return track
    }

    fun saveTrack(){
        if (checkedTrack.value!=null) {
            lastCheckedTrackInteractor.saveLastCheckedTrack(checkedTrack.value!!)
        }
    }

    // Методы для плеера

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
        resetTimer()
    }
    fun onPlayButtonClicked() {
        when (playerStateLiveData.value) {
            PlayerState.STATE_PLAYING -> pausePlayer()
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> startPlayer()
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
                playerStateLiveData.postValue(PlayerState.STATE_PREPARED)
            }

            mediaPlayer.setOnCompletionListener {

                playerStateLiveData.postValue(PlayerState.STATE_PREPARED)
                handler.removeCallbacks(timerRunnable)
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
        const val PLAYING_PROGRESS_DEBOUNCE_DELAY = 500L

        fun getFactory(track: Track?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                LibraryViewModel(app, track)
            }
        }
    }
}
