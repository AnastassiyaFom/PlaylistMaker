package com.practicum.playlistmaker.settings.domain


class SettingsInteractorImpl(private var settings: SettingsRepository): SettingsInteractor {
    override fun getDarkThemeFlag() : Boolean {
        return settings.getDarkThemeFlag()
    }
    override fun saveThemeMode(darkTheme:Boolean){
        settings.saveThemeMode(darkTheme)
    }

}