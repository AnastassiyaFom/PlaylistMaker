package com.practicum.playlistmaker.library.domain.db

import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class SelectedTracksInteractorImpl (
    private val selectedTracksRepository: SelectedTracksRepository
) : SelectedTracksInteractor {

    override fun selectedTracks(): Flow<List<Track>> {
        return selectedTracksRepository.selectedTracks()
    }
}