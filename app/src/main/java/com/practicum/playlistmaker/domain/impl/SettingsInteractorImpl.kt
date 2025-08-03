package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.interfaces.interactors.SettingsInteractor
import com.practicum.playlistmaker.domain.interfaces.repositories.SettingsRepository


class SettingsInteractorImpl(private var settings: SettingsRepository): SettingsInteractor {
    override fun getSavedDarkThemeFlag() : Boolean {
        return settings.getSavedDarkThemeFlag()
    }
    override fun saveThemeMode(darkTheme:Boolean){
        settings.saveThemeMode(darkTheme)
    }
}