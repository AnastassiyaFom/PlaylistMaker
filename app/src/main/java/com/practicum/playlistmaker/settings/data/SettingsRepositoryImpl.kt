package com.practicum.playlistmaker.settings.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(context: Context): SettingsRepository {
    private var settings: SettingsDto = SettingsDto(
        shareMessage = context.getResources().getString(R.string.share_message),
        supportMessage=context.getResources().getString(R.string.support_message),
        supportMailSubject = context.getResources().getString(R.string.support_mail_subject),
        supportMailAdress = arrayOf(context.getResources().getString(R.string.support_mail_adress)),
        userAgreementLink = context.getResources().getString(R.string.user_agreement_link),
        darkThemeFlag = false
    )
    private var sharedPrefs: SharedPreferences= context.getSharedPreferences(DARK_THEME_PREFERENCES, MODE_PRIVATE)
    init {
        settings.darkThemeFlag = sharedPrefs.getBoolean(DARK_THEME_ENABLE, false)
    }
    override fun getShareMessage(): String {
        return settings.shareMessage
    }

    override fun getSupportMessage(): String {
        return settings.supportMessage
    }

    override fun getSupportMailSubject(): String {
        return settings.supportMailSubject
    }

    override fun getSupportMailAdress(): Array<String> {
        return settings.supportMailAdress
    }

    override fun getUserAgreementLink(): String {
        return settings.userAgreementLink
    }

    override fun getDarkThemeFlag(): Boolean {
        return settings.darkThemeFlag
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