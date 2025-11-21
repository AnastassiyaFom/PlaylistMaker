package com.practicum.playlistmaker.library.domain

import android.net.Uri

data class Playlist(
    var playlstName: String,
    var playlistDescription:String = "",
    var playlistImageDir: Uri? =null,
   // var tracks:MutableList<String> = mutableListOf(),
    var tracks:String="",
    var tracksCount:Int=0
)
