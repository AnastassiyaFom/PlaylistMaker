package com.practicum.playlistmaker.main.domain

interface DarkThemeInteractor {

    fun getSavedDarkThemeFlag() : Boolean
    fun saveThemeMode(darkTheme:Boolean)
}