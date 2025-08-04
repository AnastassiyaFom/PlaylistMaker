package com.practicum.playlistmaker.domain.interfaces.interactors

interface DarkThemeInteractor {

    fun getSavedDarkThemeFlag() : Boolean
    fun saveThemeMode(darkTheme:Boolean)
}