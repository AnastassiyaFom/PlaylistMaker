package com.practicum.playlistmaker.library.ui.viewModel.playlists


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.Playlist
import com.practicum.playlistmaker.library.domain.db.PlaylistsInteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsInteractor: PlaylistsInteractor
): ViewModel() {
    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    init {
        getData()
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()){
            renderState(PlaylistsState.Empty)
        }
        else
        {
            renderState(PlaylistsState.Content(playlists))
        }
    }

    private fun renderState(state: PlaylistsState) {
        stateLiveData.postValue(state)
    }

    fun getData() {
        viewModelScope.launch {
            playlistsInteractor
                .getAllPlaylists()
                .collect { playlists ->
                    processResult(playlists)
                }
        }
    }

}