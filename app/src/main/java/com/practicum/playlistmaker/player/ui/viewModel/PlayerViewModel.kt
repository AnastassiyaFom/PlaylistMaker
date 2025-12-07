package com.practicum.playlistmaker.player.ui.viewModel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.library.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.library.domain.db.SelectedTracksInteractor
import com.practicum.playlistmaker.library.ui.viewModel.playlists.PlaylistsState
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel ( private val mediaPlayer: MediaPlayer,
                        private val dbInteractor: SelectedTracksInteractor,
                        private val playlistsInteractor: PlaylistsInteractor,


): ViewModel(){


    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observePlaylistsState(): LiveData<PlaylistsState> = stateLiveData

    private var track:Track=Track()

    private val checkedTrack = MutableLiveData<Track>()
    fun observeCheckedTrack(): MutableLiveData<Track> = checkedTrack

    private var playerStateLiveData:MutableLiveData<PlayerState>  = MutableLiveData( PlayerState.Default())
    fun observePlayerState(): MutableLiveData<PlayerState> = playerStateLiveData

    private var timerJob: Job? = null

    private val pl:MutableList<Playlist> = mutableListOf()

    init {
        getPlaylists()
    }

    // Методы для трека
    fun loadTrack(trackToPlay: Track):Track {
        track = trackToPlay
        checkedTrack.postValue(track)
        preparePlayer()
        return track
    }
    fun isTrackInFavorites(track:Track):Boolean{
        return dbInteractor.isTrackInSelected(track)
    }

    fun addToFavorites(track:Track){
        dbInteractor.insertTrack(track,true)
    }

    fun deleteFromFavorites(track:Track){
        dbInteractor.deleteTrack(track)
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
        val url:String =track.previewUrl?:""
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
        timerJob?.cancel()
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

    fun getPlaylists() {
        viewModelScope.launch {
            playlistsInteractor
                .getAllPlaylists()
                .collect { playlists ->
                    processResult(playlists)
                }
        }
    }
    private fun renderState(state: PlaylistsState) {
        stateLiveData.postValue(state)
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()){
            renderState(PlaylistsState.Empty)
        }
        else
        {
            renderState(PlaylistsState.Content(playlists))
        }
    }

    fun addTrackToPlaylist(checkedTrack: Track, playlist: Playlist) {
        playlistsInteractor.addTrackToPlaylist(checkedTrack,playlist)

    }

    fun isTrackInPlaylist(checkedTrack: Track, playlist: Playlist): Boolean {
        return playlistsInteractor.isTrackInPlaylist(checkedTrack.trackId, playlist.id)
    }


    companion object {
        const val PLAYING_PROGRESS_DEBOUNCE_DELAY = 300L
    }
}
