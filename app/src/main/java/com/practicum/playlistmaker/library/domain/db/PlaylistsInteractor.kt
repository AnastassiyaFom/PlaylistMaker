package com.practicum.playlistmaker.library.domain.db

import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun addNewPlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
    fun getTracksInPlaylist(playlist: Playlist):Flow<List<Track>>
    fun addTrackToPlaylist(track: Track, playlist: Playlist)
    fun isTrackInPlaylist(trackId: Int, playlistId:Int):Boolean

}