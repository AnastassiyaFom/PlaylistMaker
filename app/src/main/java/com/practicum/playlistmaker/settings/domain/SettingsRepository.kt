package com.practicum.playlistmaker.settings.domain

interface SettingsRepository {
    fun getShareMessage(): String
    fun getSupportMessage(): String
    fun getSupportMailSubject(): String
    fun getSupportMailAdress(): Array<String>
    fun getUserAgreementLink(): String
    fun getDarkThemeFlag():Boolean
    fun saveThemeMode(darkTheme:Boolean)
}