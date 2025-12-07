package com.practicum.playlistmaker.settings.data

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.practicum.playlistmaker.settings.domain.ExternalNavigator


class ExternalNavigatorImpl(private val context: Context):ExternalNavigator {
    override fun openLink(link: String) {
        val openlink = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        openlink.setFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(openlink)
    }

    override fun share(sharingMessage: String) {
        val shareAppIntent = Intent(Intent.ACTION_SEND)
        shareAppIntent.setType("text/plain")
        shareAppIntent.putExtra(Intent.EXTRA_TEXT, sharingMessage)
        val chooserIntent = Intent.createChooser(shareAppIntent, "Share with")
        chooserIntent.flags = FLAG_ACTIVITY_NEW_TASK
        context.startActivity(chooserIntent)
    }

    override fun sendEmail(emailData: EmailData) {
        val textToSupportIntent = Intent(Intent.ACTION_SENDTO)
        textToSupportIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        textToSupportIntent.data = Uri.parse("mailto:")
        textToSupportIntent.putExtra(Intent.EXTRA_EMAIL, emailData.supportMailAdress)
        textToSupportIntent.putExtra(Intent.EXTRA_TEXT, emailData.supportMessage)
        textToSupportIntent.putExtra(Intent.EXTRA_SUBJECT, emailData.supportMailSubject)
        context.startActivity(textToSupportIntent)
    }
}