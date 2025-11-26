package com.practicum.playlistmaker.library.domain.db

import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class SelectedTracksInteractorImpl (
    private val selectedTracksRepository: SelectedTracksRepository
) : SelectedTracksInteractor {

    override fun getSelectedTracks(): Flow<List<Track>> {
        return selectedTracksRepository.getSelectedTracks()
    }
    override fun insertTrack(track: Track, isInFavorites:Boolean) {
        selectedTracksRepository.insertTrack(track, isInFavorites)
    }

    override fun deleteTrack(track: Track) {
        selectedTracksRepository.deleteTrackFromFavorites(track)
    }

    override fun getTrackById(id: Int): Track? {
        return selectedTracksRepository.getTrackById(id)
    }

    override fun isTrackInSelected(track: Track): Boolean {
        return (selectedTracksRepository.getTrackById(track.trackId) != null)
    }
}