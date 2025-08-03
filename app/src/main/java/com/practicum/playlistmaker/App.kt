package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate


import com.practicum.playlistmaker.domain.interfaces.interactors.SettingsInteractor

import com.practicum.playlistmaker.Creator.provideSettingsInteractor

open class App : Application() {

    private var darkTheme = false

    private lateinit var settingsRepository: SettingsInteractor
    override fun onCreate() {

        super.onCreate()

        settingsRepository = provideSettingsInteractor(this)

      if (settingsRepository!=null) {
            darkTheme = settingsRepository.getSavedDarkThemeFlag()
            switchTheme(darkTheme)
        }


    }
    fun getDarkThemeFlag():Boolean{
        return darkTheme
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
       settingsRepository.saveThemeMode(darkTheme)
    }

}

/*

import android.content.SharedPreferences


class App : Application() {
    val DARK_THEME_PREFERENCES = "Dark Theme Preferences"
    val DARK_THEME_ENABLE = "Dark Theme Is Enable"
    var darkTheme = false
    lateinit var sharedPrefs:SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(DARK_THEME_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(DARK_THEME_ENABLE, false)
        switchTheme(darkTheme)
    }
    fun getDarkThemeFlag():Boolean{
        return darkTheme
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_ENABLE, darkTheme)
            .apply()
    }

}



 */