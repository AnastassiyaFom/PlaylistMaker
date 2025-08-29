package com.practicum.playlistmaker.settings.data

data class SettingsDto (
    var shareMessage: String,
    var supportMessage: String,
    var supportMailSubject: String,
    var supportMailAdress: Array<String>,
    var userAgreementLink:String,
    var darkThemeFlag:Boolean
    )