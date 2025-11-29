package com.practicum.playlistmaker.library.ui.viewModel.playlists

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.library.domain.db.PlaylistsInteractor
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.settings.domain.ExternalNavigator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



class PlaylistScreenViewModel (
    private val playlistsInteractor: PlaylistsInteractor,
    private val context: Context,
    private val navigator: ExternalNavigator
): ViewModel() {

    private val stateLiveData = MutableLiveData<SinglePlaylistState>()
    fun observeState(): LiveData<SinglePlaylistState> = stateLiveData
    private  var playlistId :Int = 0

    fun getPlaylistById(plId: Int) {
        playlistId = plId
        getTracksList(plId)
    }

    private fun getTracksList(playlistId: Int) {
        viewModelScope.launch {
            playlistsInteractor
                .getTracksInPlaylist(playlistId)
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    fun getPlaylistDurationString(): String {
        when (stateLiveData.value) {
            is SinglePlaylistState.Content -> return DurationToStringConverter((stateLiveData.value as SinglePlaylistState.Content).playlistDuration)
            else -> return DurationToStringConverter(0)
        }
    }

    fun shareTracks(message: String) {
        navigator.share(message)
    }

    private fun processResult(tracks: List<Track>) {
        val playlist = getPlaylistData()
        if (tracks.isEmpty()) {
            renderState(SinglePlaylistState.EmptyTracks(playlist, 0))
        } else {
            var durationSum = 0
            tracks.forEach {
                durationSum = durationSum + convertTimeToInt(it.trackTime)
            }
            renderState(SinglePlaylistState.Content(playlist, tracks, durationSum))
        }
    }

    private fun renderState(state: SinglePlaylistState) {
        stateLiveData.postValue(state)
    }

    private fun DurationToStringConverter(durationSum: Int): String {
        val totalDurString = SimpleDateFormat("mm", Locale.getDefault()).format(durationSum)
        return  totalDurString + " " +
                context.resources.getQuantityString(R.plurals.plural_minutes,totalDurString.toInt())
    }

    private fun convertTimeToInt(trackTime: String): Int {
        val format = SimpleDateFormat("mm:ss", Locale.getDefault())
        val date: Date = format.parse(trackTime)!!
        return date.time.toInt()
    }

    fun getTracksCountString(): String {
        val plList =
            when(stateLiveData.value){
                is SinglePlaylistState.Content -> (stateLiveData.value as SinglePlaylistState.Content).playlist
                is SinglePlaylistState.Empty -> (stateLiveData.value as SinglePlaylistState.EmptyTracks).playlist
                else ->Playlist()
            }

        val count = plList.tracksCount
        return (plList.tracksCount.toString() + " " +
                context.resources.getQuantityString(R.plurals.plural_track, count))
    }
    private  fun getPlaylistData() = playlistsInteractor.getPlaylistById(playlistId)

    fun getPlaylist():Playlist{
        when(stateLiveData.value){
            is SinglePlaylistState.Content ->
                return  (stateLiveData.value as SinglePlaylistState.Content).playlist
            is SinglePlaylistState.EmptyTracks ->
                return  (stateLiveData.value as SinglePlaylistState.EmptyTracks).playlist
            else ->
                return Playlist()
        }
    }

    fun deleteTrack(trackId: Int, playlistId: Int) {
        if (stateLiveData.value is SinglePlaylistState.Content) {
            val plList = (stateLiveData.value as SinglePlaylistState.Content).playlist
            if (plList.tracksCount==1) renderState(SinglePlaylistState.EmptyTracks(plList,0))
        }
        playlistsInteractor.deleteTrackFromPlaylist(trackId, playlistId)
        getTracksList(playlistId)

    }

    fun makeSharingMessage():String {

        when (stateLiveData.value){
            is SinglePlaylistState.Content -> {
                val playlist = (stateLiveData.value as SinglePlaylistState.Content).playlist
                val tracks: List<Track> =
                    (stateLiveData.value as SinglePlaylistState.Content).tracks
                var message = playlist.playlstName + "\n" +
                        playlist.playlistDescription + "\n" +
                        getTracksCountString() + "\n"
                var counter = 1
                tracks.forEach {
                    message += "${counter.toString()}. ${it.artistName} - ${it.trackName} (${
                        DurationToStringConverter(
                            convertTimeToInt(it.trackTime)
                        )
                    })\n"
                    counter++
                }
                return message
            }
            else -> return ""
        }
    }

    fun deletePlaylist() {
        playlistsInteractor.deletePlaylist(playlistId)

    }

}
