package com.practicum.playlistmaker.player.ui.viewModel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.App
import com.practicum.playlistmaker.creator.Creator.provideLastCheckedTrackInteractor
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.ui.view.SearchActivity.Companion.CHECKED_TRACK

class LibraryViewModel (private val context: Context): ViewModel(){
    private var  lastCheckedTrackInteractor= provideLastCheckedTrackInteractor(context)

    fun loadTrack(intent: Intent):Track?{
        var checkedTrack:Track?
        // Получаем данные о треке из intent (с активити поиска) или  из sharedPreferences,
        // если поиска еще не было в текущей сессии
        checkedTrack = intent.getParcelableExtra<Track>(CHECKED_TRACK)
        if (checkedTrack == null) {
            checkedTrack = lastCheckedTrackInteractor.getLastCheckedTrack()
        }
        return checkedTrack

    }
    fun saveTrack(checkedTrack: Track?){
        if (checkedTrack!=null) {
            lastCheckedTrackInteractor.saveLastCheckedTrack(checkedTrack)
        }

    }
    companion object {

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                LibraryViewModel(app)
            }
        }
    }
}
