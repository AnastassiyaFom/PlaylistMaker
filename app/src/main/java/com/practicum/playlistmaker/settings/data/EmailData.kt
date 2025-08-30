package com.practicum.playlistmaker.settings.data

import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R

data class EmailData (
    var supportMessage: String,
    var supportMailSubject: String,
    var supportMailAdress: Array<String>
)

