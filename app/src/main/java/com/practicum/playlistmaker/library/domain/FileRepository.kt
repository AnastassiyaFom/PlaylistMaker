package com.practicum.playlistmaker.library.domain

import android.net.Uri

interface FileRepository {
    fun saveImageToPrivateStorage(uri: Uri, albumName:String)
}