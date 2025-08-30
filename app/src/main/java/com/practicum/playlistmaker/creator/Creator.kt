package com.practicum.playlistmaker.creator

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.player.data.LastCheckedTrackRepositorySharedPrefImpl
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.PrefsStorageClient
import com.practicum.playlistmaker.player.domain.LastCheckedTrackInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractorImpl
import com.practicum.playlistmaker.search.domain.TracksHistoryInteractor
import com.practicum.playlistmaker.search.domain.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.search.domain.TracksHistoryRepositoryImpl
import com.practicum.playlistmaker.search.domain.TracksInteractorImpl
import com.practicum.playlistmaker.player.domain.LastCheckedTrackInteractor
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.player.domain.LastCheckedTrackRepository
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.settings.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.settings.data.SharingRepositoryImpl
import com.practicum.playlistmaker.settings.domain.ExternalNavigator
import com.practicum.playlistmaker.settings.domain.SharingInteractor
import com.practicum.playlistmaker.settings.domain.SharingInteractorImpl
import com.practicum.playlistmaker.settings.domain.SharingRepository

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }


    private fun getTrackHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            PrefsStorageClient<MutableList<Track>>(
            context,
            "Tracks History",
            object : TypeToken<MutableList<Track>>() {}.type)
        )
    }

    fun provideTrackHistoryInteractor(context: Context): TracksHistoryInteractor {
        return TracksHistoryRepositoryImpl(getTrackHistoryRepository(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return  SettingsInteractorImpl(getSettingsRepository(context))
    }


    private fun getLastCheckedTrackRepository(context: Context): LastCheckedTrackRepository {
        return LastCheckedTrackRepositorySharedPrefImpl(context)
    }
    fun provideLastCheckedTrackInteractor(context: Context): LastCheckedTrackInteractor {
        return  LastCheckedTrackInteractorImpl(getLastCheckedTrackRepository(context))
    }

    private fun getSharingRepository(context: Context): SharingRepository {
        return SharingRepositoryImpl(context)
    }
    private fun getExternalNavigator(context: Context):ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }
    fun provideSharingInteractor(context: Context): SharingInteractor {
        return  SharingInteractorImpl(getSharingRepository(context),
            getExternalNavigator(context))
    }



}