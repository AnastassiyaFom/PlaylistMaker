package com.practicum.playlistmaker.settings.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(context: Context): SettingsRepository {
    private var darkThemeFlag = false
    private var sharedPrefs: SharedPreferences= context.getSharedPreferences(DARK_THEME_PREFERENCES, MODE_PRIVATE)
    init {
        darkThemeFlag = sharedPrefs.getBoolean(DARK_THEME_ENABLE, false)
    }

    override fun getDarkThemeFlag(): Boolean {
        return darkThemeFlag
    }
    override fun saveThemeMode(darkTheme:Boolean){
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_ENABLE, darkTheme)
            .apply()
    }
    companion object{
        private const val  DARK_THEME_PREFERENCES = "Dark Theme Preferences"
        private const val DARK_THEME_ENABLE = "Dark Theme Is Enable"
    }

}