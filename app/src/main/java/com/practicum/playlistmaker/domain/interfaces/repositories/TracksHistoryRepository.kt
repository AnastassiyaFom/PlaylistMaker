package com.practicum.playlistmaker.domain.interfaces.repositories

import com.practicum.playlistmaker.domain.models.Track

interface TracksHistoryRepository {
    var tracksInHistory:MutableList<Track>
    val tracksInHistoryMaxLength: Int

    fun refrashHistoryInRepository()
    fun clearHistory()
    fun loadHistory(): MutableList<Track>
}