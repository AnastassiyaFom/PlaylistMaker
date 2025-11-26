package com.practicum.playlistmaker.library.data.DB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity (
    @PrimaryKey (autoGenerate = true)
    val playlistId:Int = 0,

    var playlstName: String,
    var playlistDescription:String = "",
    var playlistImageDir:String="",
    var tracks:String = "",
    var tracksCount:Int=0
)
