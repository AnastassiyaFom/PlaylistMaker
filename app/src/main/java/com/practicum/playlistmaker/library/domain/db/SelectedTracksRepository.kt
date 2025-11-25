package com.practicum.playlistmaker.library.domain.db

import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface SelectedTracksRepository {
    fun getSelectedTracks(): Flow<List<Track>>
    fun insertTrack(track: Track,isInFavorites:Boolean)
    //fun deleteTrack(track:Track)
    fun deleteTrackFromFavorites(track:Track)
    fun getTrackById(id:Int):Track?
    fun deleteTrackFromDb(track:Track)
}