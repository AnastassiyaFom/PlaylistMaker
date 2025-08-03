package com.practicum.playlistmaker.domain.interfaces.repositories

import android.content.Context

interface SettingsRepository {
    val context: Context
    fun getSavedDarkThemeFlag() : Boolean
    fun saveThemeMode(darkTheme:Boolean)
}