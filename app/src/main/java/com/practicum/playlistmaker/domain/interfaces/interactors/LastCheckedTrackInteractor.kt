package com.practicum.playlistmaker.domain.interfaces.interactors


import com.practicum.playlistmaker.domain.models.Track

interface LastCheckedTrackInteractor {

    fun getLastCheckedTrack(): Track?
    fun saveLastCheckedTrack(track: Track)
}