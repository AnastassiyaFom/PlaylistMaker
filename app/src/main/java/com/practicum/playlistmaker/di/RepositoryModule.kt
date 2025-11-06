package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.library.data.DB.TrackDbConvertor
import com.practicum.playlistmaker.library.data.SelectedTracksRepositoryImpl
import com.practicum.playlistmaker.library.domain.db.SelectedTracksRepository
import com.practicum.playlistmaker.player.domain.DBTrackRepository
import com.practicum.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.settings.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.data.SharingRepositoryImpl
import com.practicum.playlistmaker.settings.domain.ExternalNavigator
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.domain.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    factory<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(named("tracksHistoryStorageClient")))
    }

    factory <SettingsRepository>{
        SettingsRepositoryImpl(get(named("settingsStorageClient")))
    }
    single<SharingRepository>{
        SharingRepositoryImpl(androidContext())
    }
    single<ExternalNavigator>{
        ExternalNavigatorImpl(androidContext())
    }
    factory { TrackDbConvertor() }
    single <SelectedTracksRepository> {
        SelectedTracksRepositoryImpl(get(),get())
    }
    single <DBTrackRepository> {
        SelectedTracksRepositoryImpl(get(),get())
    }


}
