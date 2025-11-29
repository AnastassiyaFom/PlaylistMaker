package com.practicum.playlistmaker.library.ui.viewModel.playlists

import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.search.domain.Track


sealed interface SinglePlaylistState {
    object Empty : SinglePlaylistState
    data class Content(
        val playlist: Playlist,
        val tracks:List<Track>,
        val playlistDuration:Int
    ) : SinglePlaylistState
    data class EmptyTracks(
        val playlist: Playlist,
        val playlistDuration:Int
    ) : SinglePlaylistState
}
