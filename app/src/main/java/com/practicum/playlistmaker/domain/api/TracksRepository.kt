package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TracksRepository {
    var resultCode:Int
    fun searchTracks(expression: String): List<Track>
}