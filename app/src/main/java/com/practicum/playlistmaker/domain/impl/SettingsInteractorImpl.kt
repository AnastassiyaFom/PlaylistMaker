package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.interfaces.interactors.DarkThemeInteractor
import com.practicum.playlistmaker.domain.interfaces.repositories.DarkThemeRepository


class SettingsInteractorImpl(private var settings: DarkThemeRepository): DarkThemeInteractor {
    override fun getSavedDarkThemeFlag() : Boolean {
        return settings.getSavedDarkThemeFlag()
    }
    override fun saveThemeMode(darkTheme:Boolean){
        settings.saveThemeMode(darkTheme)
    }
}