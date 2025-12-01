package com.practicum.playlistmaker.library.domain

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    var id:Int = 0,
    var playlstName: String,
    var playlistDescription:String = "",
    var playlistImageDir: Uri? =null,
   // var tracks:MutableList<String> = mutableListOf(),
    var tracks:String="",
    var tracksCount:Int=0
    //val trackstime:
):Parcelable{
    fun isEmpty():Boolean{
        return (playlstName.isEmpty()  )
    }

    fun isNotEmpty():Boolean{
        return (!isEmpty())
    }

    constructor():  this(
        id = 0,
        playlstName = "",
        playlistDescription = "",
        playlistImageDir = null,
        tracks = "",
        tracksCount = 0
    )

}
