package com.practicum.playlistmaker.settings.ui


import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel

import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SharingInteractor


class SettingsViewModel(private val sharingInteractor: SharingInteractor,
                        private val settingsInteractor: SettingsInteractor) : ViewModel() {
    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun textToSupport() {
        sharingInteractor.openSupport()
    }

    fun openUserAgreement() {
        sharingInteractor.openTerms()
    }

    fun saveTheme(darkTheme:Boolean) {
        settingsInteractor.saveThemeMode(darkTheme)

            AppCompatDelegate.setDefaultNightMode(
                if (darkTheme) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )

    }
     fun getDarkThemeFlag() : Boolean {
        return settingsInteractor.getDarkThemeFlag()
    }

}