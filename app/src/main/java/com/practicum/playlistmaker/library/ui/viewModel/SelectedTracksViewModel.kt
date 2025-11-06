package com.practicum.playlistmaker.library.ui.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.db.SelectedTracksInteractor
import com.practicum.playlistmaker.search.domain.Track
import kotlinx.coroutines.launch


class SelectedTracksViewModel(
    private val selectedTrackInteractor: SelectedTracksInteractor
) : ViewModel()
{
    private val stateLiveData = MutableLiveData<SelectedTracksState>()
    fun observeState(): LiveData<SelectedTracksState> = stateLiveData

    init {
        getData()
    }
    fun getData(){
        viewModelScope.launch {
            selectedTrackInteractor
                .selectedTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }
    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()){
            renderState(SelectedTracksState.Empty)
        }
        else
        {
            renderState(SelectedTracksState.Content(tracks))
        }
    }

    private fun renderState(state: SelectedTracksState) {
        stateLiveData.postValue(state)
    }
}
