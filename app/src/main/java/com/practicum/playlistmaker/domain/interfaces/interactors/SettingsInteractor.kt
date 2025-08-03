package com.practicum.playlistmaker.domain.interfaces.interactors

interface SettingsInteractor {

    fun getSavedDarkThemeFlag() : Boolean
    fun saveThemeMode(darkTheme:Boolean)
}