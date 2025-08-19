package com.practicum.playlistmaker.domain.interfaces.repositories

interface DarkThemeRepository {

    fun getSavedDarkThemeFlag() : Boolean
    fun saveThemeMode(darkTheme:Boolean)
}