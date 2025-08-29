package com.practicum.playlistmaker.search.domain

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