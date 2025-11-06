package com.practicum.playlistmaker.di


import com.practicum.playlistmaker.library.domain.db.SelectedTracksInteractor
import com.practicum.playlistmaker.library.domain.db.SelectedTracksInteractorImpl
import com.practicum.playlistmaker.player.domain.DBTrackInteractor
import com.practicum.playlistmaker.player.domain.DBTrackInteractorImpl
import com.practicum.playlistmaker.search.domain.TracksHistoryInteractor
import com.practicum.playlistmaker.search.domain.TracksHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.SharingInteractor
import com.practicum.playlistmaker.settings.domain.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory <TracksInteractor>{
        TracksInteractorImpl(get())
    }
    factory <TracksHistoryInteractor>{
        TracksHistoryInteractorImpl(get())
    }



    factory<SharingInteractor>{
        SharingInteractorImpl(get(),get())
    }
    factory<SettingsInteractor>{
        SettingsInteractorImpl(get())
    }
    single <SelectedTracksInteractor>{
         SelectedTracksInteractorImpl(get())
    }

    single <DBTrackInteractor>{
        DBTrackInteractorImpl(get())
    }


}
