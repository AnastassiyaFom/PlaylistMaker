package com.practicum.playlistmaker.player.domain

import com.practicum.playlistmaker.search.domain.Track

interface DBTrackRepository {
    fun insertTrack(track: Track)
    fun deleteTrack(track:Track)
    fun getTrackById(id:Int):Track?
}