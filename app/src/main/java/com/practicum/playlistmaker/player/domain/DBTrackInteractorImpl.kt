package com.practicum.playlistmaker.player.domain

import com.practicum.playlistmaker.search.domain.Track

class DBTrackInteractorImpl(
   private val trackRepository : DBTrackRepository
): DBTrackInteractor{
    override fun insertTrack(track: Track) {
        trackRepository.insertTrack(track)
    }

    override fun deleteTrack(track: Track) {
        trackRepository.deleteTrack(track)
    }

    override fun getTrackById(id: Int): Track? {
        return trackRepository.getTrackById(id)
    }

    override fun isTrackInSelected(track: Track): Boolean {
        return (trackRepository.getTrackById(track.trackId) != null)
    }

}