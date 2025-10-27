package com.practicum.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    var resultCode:Int
    fun searchTracks(expression: String): Flow<List<Track>>
}