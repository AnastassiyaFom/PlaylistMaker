package com.practicum.playlistmaker.library.ui.viewModel

import com.practicum.playlistmaker.search.domain.Track

sealed interface SelectedTracksState {
    object Empty : SelectedTracksState
    data class Content(
        val tracks: List<Track>
    ) : SelectedTracksState
}