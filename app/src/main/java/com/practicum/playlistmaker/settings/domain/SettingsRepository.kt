package com.practicum.playlistmaker.settings.domain

interface SettingsRepository {
    fun getDarkThemeFlag():Boolean
    fun saveThemeMode(darkTheme:Boolean)
}