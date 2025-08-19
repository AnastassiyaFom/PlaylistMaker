package com.practicum.playlistmaker.domain.impl


import com.practicum.playlistmaker.domain.interfaces.interactors.SettingsInteractor
import com.practicum.playlistmaker.domain.interfaces.repositories.SettingsRepository


class SettingsInteractorImpl(private var settings: SettingsRepository): SettingsInteractor {
    override fun getDarkThemeFlag() : Boolean {
        return settings.getDarkThemeFlag()
    }
    override fun saveThemeMode(darkTheme:Boolean){
        settings.saveThemeMode(darkTheme)
    }
    override fun getShareMessage(): String {
        return settings.getShareMessage()
    }

    override fun getSupportMessage(): String {
        return settings.getSupportMessage()
    }

    override fun getSupportMailSubject(): String {
        return settings.getSupportMailSubject()
    }

    override fun getSupportMailAdress(): Array<String> {
        return settings.getSupportMailAdress()
    }

    override fun getUserAgreementLink(): String {
        return settings.getUserAgreementLink()
    }


}