package com.practicum.playlistmaker.domain.interfaces.repositories

import com.practicum.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    val tracksInHistoryMaxLength: Int
    fun writeDataToRepository(data:MutableList<Track>)
    fun clearHistory()
    fun getTracksFromHistory():MutableList<Track>
}