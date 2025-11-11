package com.practicum.playlistmaker.library.domain.db

import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface SelectedTracksRepository {
    fun selectedTracks(): Flow<List<Track>>
    fun insertTrack(track: Track)
    fun deleteTrack(track:Track)
    fun getTrackById(id:Int):Track?
}