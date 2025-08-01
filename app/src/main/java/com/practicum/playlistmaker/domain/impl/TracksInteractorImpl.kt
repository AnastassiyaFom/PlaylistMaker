package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository


class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        val t = Thread {
            val foundTracks = repository.searchTracks(expression)
            consumer.consume(foundTracks,repository.resultCode)
        }
        t.start()
    }
}
