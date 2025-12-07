package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.library.data.DB.AppDatabase
import com.practicum.playlistmaker.library.data.DB.PlaylistDBConverter
import com.practicum.playlistmaker.library.data.DB.PlaylistTrackDAO
import com.practicum.playlistmaker.library.data.DB.PlaylistsDao
import com.practicum.playlistmaker.library.data.DB.SelectedTrackDbConvertor
import com.practicum.playlistmaker.library.data.DB.SelectedTracksDao
import com.practicum.playlistmaker.library.data.DB.TrackDbConvertor
import com.practicum.playlistmaker.library.data.DB.TracksDao
import com.practicum.playlistmaker.library.data.FileRepositoryImpl
import com.practicum.playlistmaker.library.data.PlaylistsRepositoryImpl
import com.practicum.playlistmaker.library.data.SelectedTracksRepositoryImpl
import com.practicum.playlistmaker.library.domain.FileRepository
import com.practicum.playlistmaker.library.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.library.domain.db.SelectedTracksRepository
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
    factory <SelectedTrackDbConvertor>{ SelectedTrackDbConvertor() }

    factory <TrackDbConvertor>{ TrackDbConvertor() }

    factory <PlaylistDBConverter>{ PlaylistDBConverter() }

    factory <SelectedTracksDao> { get<AppDatabase>().selectedTracksDao() }

    factory <PlaylistsDao>{ get<AppDatabase>().playlistsDao() }

    factory <TracksDao>{ get<AppDatabase>().tracksDao() }

    factory <PlaylistTrackDAO>{ get<AppDatabase>().playlistTrackDao() }


    factory <SelectedTracksRepository> {
        SelectedTracksRepositoryImpl(get(),get())
    }

    factory <PlaylistsRepository> {
        PlaylistsRepositoryImpl(get(), get(), get(), get(), get() )
    }
    factory <FileRepository>{
        FileRepositoryImpl(get())
    }

}
