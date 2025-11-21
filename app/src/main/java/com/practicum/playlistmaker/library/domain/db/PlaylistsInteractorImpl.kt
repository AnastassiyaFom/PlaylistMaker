package com.practicum.playlistmaker.library.domain.db

import com.practicum.playlistmaker.library.domain.Playlist
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
}