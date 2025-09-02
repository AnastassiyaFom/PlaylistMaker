package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.data.EmailData

interface ExternalNavigator {
    fun openLink(link:String)
    fun  shareLink(sharingMessage:String)
    fun sendEmail(emailData:EmailData)
}