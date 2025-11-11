package com.practicum.playlistmaker.library.domain.db

import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class SelectedTracksInteractorImpl (
    private val selectedTracksRepository: SelectedTracksRepository
) : SelectedTracksInteractor {

    override fun selectedTracks(): Flow<List<Track>> {
        return selectedTracksRepository.selectedTracks()
    }
    override fun insertTrack(track: Track) {
        selectedTracksRepository.insertTrack(track)
    }

    override fun deleteTrack(track: Track) {
        selectedTracksRepository.deleteTrack(track)
    }

    override fun getTrackById(id: Int): Track? {
        return selectedTracksRepository.getTrackById(id)
    }

    override fun isTrackInSelected(track: Track): Boolean {
        return (selectedTracksRepository.getTrackById(track.trackId) != null)
    }
}