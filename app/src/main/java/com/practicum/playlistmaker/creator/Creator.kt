package com.practicum.playlistmaker.creator

import android.content.Context
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.settings.data.SharingRepositoryImpl
import com.practicum.playlistmaker.settings.domain.ExternalNavigator
import com.practicum.playlistmaker.settings.domain.SharingInteractor
import com.practicum.playlistmaker.settings.domain.SharingInteractorImpl
import com.practicum.playlistmaker.settings.domain.SharingRepository

object Creator {



    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return  SettingsInteractorImpl(getSettingsRepository(context))
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