package com.practicum.playlistmaker.creator

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator.provideSettingsInteractor
import com.practicum.playlistmaker.domain.interfaces.interactors.SettingsInteractor

class App : Application() {

    private var darkTheme = false

    private lateinit var settingsInteractor: SettingsInteractor
    override fun onCreate() {

      super.onCreate()

      settingsInteractor = provideSettingsInteractor(this)

      if (settingsInteractor!=null) {
            darkTheme = settingsInteractor.getDarkThemeFlag()
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
