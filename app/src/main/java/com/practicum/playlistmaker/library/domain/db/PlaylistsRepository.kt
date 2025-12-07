package com.practicum.playlistmaker.library.domain.db

import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun addNewPlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
    fun getTracksInPlaylist(playlistId: Int):Flow<List<Track>>
    fun addTrackToPlaylist(track:Track, playlist: Playlist)
    fun deleteTrackFromPlaylist(trackId: Int, playlistId:Int)
    fun isTrackInPlaylist(trackId: Int, playlistId:Int):Boolean
    fun getPlaylistById(playlistId: Int):Playlist
    fun deletePlaylist(playlistId: Int)
    fun refrashDataInDb(plListId:Int,plList:Playlist)

}