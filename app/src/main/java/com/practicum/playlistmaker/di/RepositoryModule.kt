package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.LastCheckedTrackRepositorySharedPrefImpl
import com.practicum.playlistmaker.player.domain.LastCheckedTrackRepository
import com.practicum.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.TracksRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    factory<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(named("tracksHistoryStorageClient")))
    }
    factory<LastCheckedTrackRepository> {
        LastCheckedTrackRepositorySharedPrefImpl(get(named("checkedTrackStorageClient" )) )

    }

}
