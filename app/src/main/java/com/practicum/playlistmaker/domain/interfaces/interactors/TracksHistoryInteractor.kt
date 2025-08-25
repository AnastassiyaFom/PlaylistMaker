package com.practicum.playlistmaker.domain.interfaces.interactors

import com.practicum.playlistmaker.domain.models.Track

interface TracksHistoryInteractor  {
    fun getTracksFromHistory(): MutableList<Track>
    fun getTracksInHistoryMaxLength(): Int
    fun clearHistory()
    fun addTrackToHistory(track: Track)
/*
    interface HistoryConsumer {
        fun consume(searchHistory: MutableList<Track>?)
    }

 */
}