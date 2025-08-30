package com.practicum.playlistmaker.settings.data

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.SharingRepository

class SharingRepositoryImpl(private val context: Context): SharingRepository {
    override fun getShareMessage(): String {
        return context.getResources().getString(R.string.share_message)
    }

    override fun getSupportEmailData(): EmailData {
        return EmailData(supportMessage=context.getResources().getString(R.string.support_message),
            supportMailSubject = context.getResources().getString(R.string.support_mail_subject),
            supportMailAdress = arrayOf(context.getResources().getString(R.string.support_mail_adress)),
        )
    }

    override fun getUserAgreementLink(): String {
        return context.getResources().getString(R.string.user_agreement_link)
    }
}