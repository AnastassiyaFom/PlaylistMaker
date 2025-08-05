package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.interfaces.interactors.LastCheckedTrackInteractor
import com.practicum.playlistmaker.domain.interfaces.repositories.LastCheckedTrackRepository
import com.practicum.playlistmaker.domain.models.Track

class LastCheckedTrackInteractorImpl(val tracksRepository:LastCheckedTrackRepository): LastCheckedTrackInteractor {
    override fun getLastCheckedTrack(): Track? {
        return tracksRepository.getLastCheckedTrack()
    }

    override fun saveLastCheckedTrack(track: Track) {
        tracksRepository.saveLastCheckedTrack(track)
    }
}