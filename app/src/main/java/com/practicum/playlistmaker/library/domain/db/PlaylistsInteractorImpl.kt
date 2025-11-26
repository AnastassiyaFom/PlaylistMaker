package com.practicum.playlistmaker.library.domain.db

import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    val playlistRepository: PlaylistsRepository
):PlaylistsInteractor {
    override fun addNewPlaylist(playlist: Playlist) {
        playlistRepository.addNewPlaylist(playlist)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylists()
    }

    override fun getTracksInPlaylist(playlist: Playlist): Flow<List<Track>> {
       return playlistRepository.getTracksInPlaylist(playlist)
    }

    override fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistRepository.addTrackToPlaylist(track,playlist)
    }
    override fun isTrackInPlaylist(trackId: Int, playlistId:Int):Boolean{
       return  playlistRepository.isTrackInPlaylist(trackId,playlistId)
    }
}