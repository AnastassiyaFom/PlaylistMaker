package com.practicum.playlistmaker

import android.content.Context
import com.practicum.playlistmaker.data.Repositories.LastCheckedTrackRepositorySharedPrefImpl
import com.practicum.playlistmaker.data.Repositories.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.Repositories.TracksHistoryRepositorySharedPrefImpl
import com.practicum.playlistmaker.data.Repositories.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.impl.LastCheckedTrackInteractorImpl
import com.practicum.playlistmaker.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.interfaces.interactors.TracksHistoryInteractor
import com.practicum.playlistmaker.domain.interfaces.repositories.TracksHistoryRepository
import com.practicum.playlistmaker.domain.interfaces.interactors.TracksInteractor
import com.practicum.playlistmaker.domain.interfaces.repositories.TracksRepository
import com.practicum.playlistmaker.domain.impl.TracksHistoryRepositoryImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.domain.interfaces.interactors.LastCheckedTrackInteractor
import com.practicum.playlistmaker.domain.interfaces.interactors.SettingsInteractor
import com.practicum.playlistmaker.domain.interfaces.repositories.LastCheckedTrackRepository
import com.practicum.playlistmaker.domain.interfaces.repositories.SettingsRepository

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }


    private fun getTrackHistoryRepository(context: Context): TracksHistoryRepository {
        return TracksHistoryRepositorySharedPrefImpl(context)
    }

    fun provideTrackHistoryInteractor(context: Context): TracksHistoryInteractor {
        return TracksHistoryRepositoryImpl(getTrackHistoryRepository(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
    fun provideSettingsInteractor(context: Context):SettingsInteractor {
        return  SettingsInteractorImpl(getSettingsRepository(context))
    }


    private fun getLastCheckedTrackRepository(context: Context): LastCheckedTrackRepository {
        return LastCheckedTrackRepositorySharedPrefImpl(context)
    }
    fun provideLastCheckedTrackInteractor(context: Context): LastCheckedTrackInteractor {
        return  LastCheckedTrackInteractorImpl(getLastCheckedTrackRepository(context))
    }



}