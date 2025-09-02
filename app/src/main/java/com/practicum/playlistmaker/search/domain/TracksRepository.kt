package com.practicum.playlistmaker.search.domain

interface TracksRepository {
    var resultCode:Int
    fun searchTracks(expression: String): List<Track>
}