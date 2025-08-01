package com.practicum.playlistmaker.data.Repositories

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.practicum.playlistmaker.domain.api.SettingsRepository

class SettingsRepositoryImpl (override val context : Context) : SettingsRepository {

    val DARK_THEME_PREFERENCES = "Dark Theme Preferences"
    val DARK_THEME_ENABLE = "Dark Theme Is Enable"

    var sharedPrefs: SharedPreferences = context.getSharedPreferences(DARK_THEME_PREFERENCES, MODE_PRIVATE)

    override fun getSavedDarkThemeFlag() : Boolean {
        return sharedPrefs.getBoolean(DARK_THEME_ENABLE, false)
    }
    override fun saveThemeMode(darkTheme:Boolean){
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_ENABLE, darkTheme)
            .apply()
    }

}