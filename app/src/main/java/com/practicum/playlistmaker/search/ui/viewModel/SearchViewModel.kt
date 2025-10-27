package com.practicum.playlistmaker.search.ui.viewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TracksHistoryInteractor
import com.practicum.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchViewModel (private val tracksInteractor: TracksInteractor,
                       private val tracksHistoryInteractor: TracksHistoryInteractor
): ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private var latestSearchRequest:String?=null
    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData
    private val historyLiveData = MutableLiveData<MutableList<Track>>()
    private var searchJob: Job? = null
    fun observeHistory(): LiveData<MutableList<Track>> = historyLiveData

    init {
        renderHistory(tracksHistoryInteractor.getTracksFromHistory())
    }


    private fun searchRequestToNet(newSearchText: String){

        if (newSearchText.isNotEmpty()) {
            renderState(
                TracksState.Loading
            )
            tracksInteractor.searchTracks(newSearchText, consumer)
        }
    }
    private val consumer = object : TracksInteractor.TracksConsumer {
        override fun consume(foundTracks: List<Track>, resultCode: Int) {//
            handler.post {
                val tracks = mutableListOf<Track>()
                if (foundTracks != null) {
                    tracks.addAll(foundTracks)
                }
                if (resultCode >=500){
                    renderState(
                        TracksState.Error(ErrorType.ERROR_NO_INTERNET)
                    )
                } else if (resultCode >= 200 && resultCode < 300 && foundTracks.isNotEmpty() == true && foundTracks != null) {
                    renderState(
                        TracksState.Content(tracks)
                    )
                } else {
                    renderState(
                        TracksState.Empty(true)
                    )
                }
            }

        }
    }



    fun searchDebounce(changedText: String) {
        if (latestSearchRequest == changedText && !(stateLiveData.value is TracksState.Error)) {
            return
        }

        this.latestSearchRequest = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequestToNet(changedText)

        }
    }

    private fun renderState(state: TracksState) {
            stateLiveData.postValue(state)
    }
    private fun renderHistory(history:MutableList<Track>){
        historyLiveData.postValue(history)
    }

    fun clearHistory()
    {
        tracksHistoryInteractor.clearHistory()
        renderHistory(tracksHistoryInteractor.getTracksFromHistory())
    }

    override fun onCleared() {
            super.onCleared()
            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun chooseTrack(track: Track) {
        tracksHistoryInteractor.addTrackToHistory(track)
        renderHistory(tracksHistoryInteractor.getTracksFromHistory())
    }

    fun destroy() {

        renderState(
            TracksState.WaitingForRequest
        )

    }

    fun getTracksInHistoryMaxLength(): Int {
        return tracksHistoryInteractor.getTracksInHistoryMaxLength()?:1
    }



    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }


}




