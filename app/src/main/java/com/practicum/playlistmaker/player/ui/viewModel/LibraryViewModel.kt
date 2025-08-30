package com.practicum.playlistmaker.player.ui.viewModel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.App
import com.practicum.playlistmaker.creator.Creator.provideLastCheckedTrackInteractor
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.ui.view.SearchActivity.Companion.CHECKED_TRACK
import com.practicum.playlistmaker.search.ui.viewModel.TracksState

class LibraryViewModel (private val context: Context,
    private val intent:Intent): ViewModel(){
    private var  lastCheckedTrackInteractor= provideLastCheckedTrackInteractor(context)
    private val checkedTrack = MutableLiveData<Track?>()
    fun observeCheckedTrack(): MutableLiveData<Track?> = checkedTrack
    init{
        var track:Track?
        // Получаем данные о треке из intent (с активити поиска) или  из sharedPreferences,
        // если поиска еще не было в текущей сессии
        track = intent.getParcelableExtra<Track>(CHECKED_TRACK)
        if (track == null) {
            track = lastCheckedTrackInteractor.getLastCheckedTrack()
        }
        checkedTrack.postValue(track)
    }

    fun saveTrack(){
        if (checkedTrack.value!=null) {
            lastCheckedTrackInteractor.saveLastCheckedTrack(checkedTrack.value!!)
        }

    }
    companion object {

        fun getFactory(intent: Intent): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                LibraryViewModel(app, intent)
            }
        }
    }
}
