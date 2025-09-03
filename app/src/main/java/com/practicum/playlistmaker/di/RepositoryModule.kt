package com.practicum.playlistmaker.di

import android.content.Context.MODE_PRIVATE
import com.practicum.playlistmaker.player.data.LastCheckedTrackRepositorySharedPrefImpl
import com.practicum.playlistmaker.player.domain.LastCheckedTrackRepository
import com.practicum.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.TracksRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    factory<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }
    factory<LastCheckedTrackRepository> {
        LastCheckedTrackRepositorySharedPrefImpl(get(),
            get(
                named("checkedTrackPreferences" )
            )
        )
    }

}
