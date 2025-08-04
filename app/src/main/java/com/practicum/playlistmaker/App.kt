package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate


import com.practicum.playlistmaker.domain.interfaces.interactors.DarkThemeInteractor

import com.practicum.playlistmaker.Creator.provideSettingsInteractor

class App : Application() {

    private var darkTheme = false

    private lateinit var settingsInteractor: DarkThemeInteractor
    override fun onCreate() {

        super.onCreate()

        settingsInteractor = provideSettingsInteractor(this)

      if (settingsInteractor!=null) {
            darkTheme = settingsInteractor.getSavedDarkThemeFlag()
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
        settingsInteractor.saveThemeMode(darkTheme)
    }

}
