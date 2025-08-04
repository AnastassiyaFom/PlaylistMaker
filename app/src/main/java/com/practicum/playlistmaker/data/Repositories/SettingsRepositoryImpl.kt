package com.practicum.playlistmaker.data.Repositories

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

import com.practicum.playlistmaker.domain.interfaces.repositories.SettingsRepository

class SettingsRepositoryImpl ( val context : Context) : SettingsRepository {

    val DARK_THEME_PREFERENCES = "Dark Theme Preferences"
    val DARK_THEME_ENABLE = "Dark Theme Is Enable"
    private lateinit var sharedPrefs: SharedPreferences
    init {
        try{
            sharedPrefs = context.getSharedPreferences(DARK_THEME_PREFERENCES, MODE_PRIVATE)
        }
        catch(e: Exception){}
    }


    override fun getSavedDarkThemeFlag() : Boolean {
        return sharedPrefs.getBoolean(DARK_THEME_ENABLE, false)
    }
    override fun saveThemeMode(darkTheme:Boolean){
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_ENABLE, darkTheme)
            .apply()
    }

}