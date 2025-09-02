package com.practicum.playlistmaker.player.domain

import com.practicum.playlistmaker.search.domain.Track

class LastCheckedTrackInteractorImpl(val tracksRepository: LastCheckedTrackRepository):
    LastCheckedTrackInteractor {
    override fun getLastCheckedTrack(): Track? {
        return tracksRepository.getLastCheckedTrack()
    }

    override fun saveLastCheckedTrack(track: Track) {
        tracksRepository.saveLastCheckedTrack(track)
    }
}