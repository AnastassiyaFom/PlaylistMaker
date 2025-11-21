package com.practicum.playlistmaker.library.domain.db

import com.practicum.playlistmaker.library.domain.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun addNewPlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
}