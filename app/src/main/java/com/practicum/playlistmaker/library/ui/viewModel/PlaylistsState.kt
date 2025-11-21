package com.practicum.playlistmaker.library.ui.viewModel

import com.practicum.playlistmaker.library.domain.Playlist


sealed interface PlaylistsState {
    object Empty : PlaylistsState
    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState
}
