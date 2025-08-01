package com.practicum.playlistmaker

import android.content.Context
import com.practicum.playlistmaker.data.Repositories.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.Repositories.TracksHistoryRepositorySharedPrefImpl
import com.practicum.playlistmaker.data.Repositories.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.SettingsRepository
import com.practicum.playlistmaker.domain.api.TracksHistoryRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun getTrackHistoryRepository(context: Context): TracksHistoryRepository {
        return TracksHistoryRepositorySharedPrefImpl(context)
    }

    fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

}