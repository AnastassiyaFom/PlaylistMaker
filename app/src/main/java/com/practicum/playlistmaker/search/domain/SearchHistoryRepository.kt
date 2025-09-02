package com.practicum.playlistmaker.search.domain

interface SearchHistoryRepository {
    val tracksInHistoryMaxLength: Int
    fun writeDataToRepository(data:MutableList<Track>)
    fun clearHistory()
    fun getTracksFromHistory():MutableList<Track>
}