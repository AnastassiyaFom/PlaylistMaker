package com.practicum.playlistmaker.domain.interfaces.repositories

import com.practicum.playlistmaker.domain.models.Track

interface TracksRepository {
    var resultCode:Int
    fun searchTracks(expression: String): List<Track>
}