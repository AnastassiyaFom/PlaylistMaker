package com.practicum.playlistmaker.player.domain


import com.practicum.playlistmaker.search.domain.Track

interface LastCheckedTrackInteractor {

    fun getLastCheckedTrack(): Track?
    fun saveLastCheckedTrack(track: Track)
}