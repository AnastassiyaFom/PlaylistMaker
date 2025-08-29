package com.practicum.playlistmaker.main.domain

interface DarkThemeRepository {

    fun getSavedDarkThemeFlag() : Boolean
    fun saveThemeMode(darkTheme:Boolean)
}