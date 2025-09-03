package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.domain.LastCheckedTrackInteractor
import com.practicum.playlistmaker.player.domain.LastCheckedTrackInteractorImpl
import com.practicum.playlistmaker.search.domain.TracksHistoryInteractor
import com.practicum.playlistmaker.search.domain.TracksHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory <TracksInteractor>{
        TracksInteractorImpl(get())
    }
    factory <TracksHistoryInteractor>{
        TracksHistoryInteractorImpl(get())
    }

    factory <LastCheckedTrackInteractor>{
        LastCheckedTrackInteractorImpl(get())
    }


}
