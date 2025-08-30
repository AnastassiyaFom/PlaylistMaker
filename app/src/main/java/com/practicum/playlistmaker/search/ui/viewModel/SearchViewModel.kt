package com.practicum.playlistmaker.search.ui.viewModel


import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

import com.practicum.playlistmaker.creator.App
import com.practicum.playlistmaker.creator.Creator.provideTrackHistoryInteractor
import com.practicum.playlistmaker.creator.Creator.provideTracksInteractor
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor


class SearchViewModel (private val context: Context): ViewModel() {
    private val tracksInteractor: TracksInteractor = provideTracksInteractor()
    private val tracksHistoryInteractor = provideTrackHistoryInteractor(context)
    private val handler = Handler(Looper.getMainLooper())


    private var latestSearchRequest:String?=null
    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData
    private val historyLiveData = MutableLiveData<MutableList<Track>>()

    fun observeHistory(): LiveData<MutableList<Track>> = historyLiveData

    init {
        renderHistory(tracksHistoryInteractor.getTracksFromHistory())
    }

    private val consumer = object : TracksInteractor.TracksConsumer {
        override fun consume(foundTracks: List<Track>, resultCode: Int) {//
            handler.post {
                val tracks = mutableListOf<Track>()
                if (foundTracks != null) {
                    tracks.addAll(foundTracks)
                }
                if (resultCode == 400) {
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

    private fun searchRequestToNet(newSearchText: String){

        if (newSearchText.isNotEmpty()) {
            renderState(
                TracksState.Loading
            )
            tracksInteractor.searchTracks(newSearchText, consumer)
        }
    }


    fun searchDebounce(changedText: String) {
        if (latestSearchRequest == changedText && !(stateLiveData.value is TracksState.Error)) {
            return
        }
        this.latestSearchRequest = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequestToNet(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
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
        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                SearchViewModel(app)
            }
        }
    }


}




