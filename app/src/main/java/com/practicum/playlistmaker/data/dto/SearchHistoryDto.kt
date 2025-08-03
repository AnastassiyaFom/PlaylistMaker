package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.domain.models.Track

data class SearchHistoryDto(
    val tracksInHistory: MutableList<Track> = mutableListOf()
)
