package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TracksHistoryRepository {

    fun loadHistory()
    fun getTracksFromHistory(): MutableList<Track>
    fun getTracksInHistoryMaxLength(): Int
    fun refrashHistoryInRepository()
    fun clearHistory()
    fun addTrackToHistory(track: Track)
}