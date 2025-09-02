package com.practicum.playlistmaker.settings.domain

interface SettingsInteractor {

    fun getDarkThemeFlag():Boolean
    fun saveThemeMode(darkTheme:Boolean)
}