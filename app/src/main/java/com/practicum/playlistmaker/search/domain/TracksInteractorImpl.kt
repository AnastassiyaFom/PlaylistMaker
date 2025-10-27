package com.practicum.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    override fun searchTracks(expression: String) : Flow<Pair<List<Track>?, Int>> {
       return  repository.searchTracks(expression).map {
            Pair(it, repository.resultCode)
        }
    }
}
