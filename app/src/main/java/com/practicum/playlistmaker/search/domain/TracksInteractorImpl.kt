package com.practicum.playlistmaker.search.domain


class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        val t = Thread {
            val foundTracks = repository.searchTracks(expression)
            consumer.consume(foundTracks,repository.resultCode)
        }
        t.start()
    }
}
