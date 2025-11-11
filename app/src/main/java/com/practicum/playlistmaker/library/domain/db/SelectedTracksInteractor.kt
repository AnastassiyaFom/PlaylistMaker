package com.practicum.playlistmaker.library.domain.db

import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface SelectedTracksInteractor {
    fun selectedTracks(): Flow<List<Track>>
    fun insertTrack(track: Track)
    fun deleteTrack(track:Track)
    fun getTrackById(id:Int):Track?
    fun isTrackInSelected(track: Track):Boolean
}