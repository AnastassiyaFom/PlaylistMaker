package com.practicum.playlistmaker.search.ui.viewModel

import com.practicum.playlistmaker.search.domain.Track

sealed interface TracksState {

    object Loading : TracksState


    data class Content(
        val tracks: MutableList<Track>
    ) : TracksState

    data class Error(
        val error: ErrorType
    ) : TracksState

    data class Empty(
        val isEmpty: Boolean = false
    ) : TracksState

    object  WaitingForRequest: TracksState
}

enum class ErrorType {
    ERROR_NO_INTERNET,
    ERROR_TRACKS_NOT_FOUND
}