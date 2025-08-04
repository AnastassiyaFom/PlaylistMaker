package com.practicum.playlistmaker.domain.interfaces.repositories

interface SettingsRepository {

    fun getSavedDarkThemeFlag() : Boolean
    fun saveThemeMode(darkTheme:Boolean)
}