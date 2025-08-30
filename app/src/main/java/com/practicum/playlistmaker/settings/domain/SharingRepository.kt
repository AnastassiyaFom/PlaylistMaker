package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.data.EmailData

interface SharingRepository {
    fun getShareMessage(): String
    fun getSupportEmailData(): EmailData
    fun getUserAgreementLink(): String
}