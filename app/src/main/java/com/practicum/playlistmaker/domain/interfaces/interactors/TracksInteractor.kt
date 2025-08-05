package com.practicum.playlistmaker.domain.interfaces.interactors

import com.practicum.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>, resultCode:Int)
    }
}

