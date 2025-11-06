package com.practicum.playlistmaker.player.domain

import com.practicum.playlistmaker.search.domain.Track

interface DBTrackInteractor {
    fun insertTrack(track: Track)
    fun deleteTrack(track:Track)
    fun getTrackById(id:Int):Track?
    fun isTrackInSelected(track: Track):Boolean
}