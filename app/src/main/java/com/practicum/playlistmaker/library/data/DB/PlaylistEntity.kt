package com.practicum.playlistmaker.library.data.DB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity (
    @PrimaryKey (autoGenerate = true)
    val playlistId:Int = 0,
    val playlstName: String,
    val playlistDescription:String ,
    val playlistImageDir:String,
    val tracks:String,
    val tracksCount:Int=0
)
{
    constructor():  this(
        playlistId = 0,
        playlstName = "",
        playlistDescription = "",
        playlistImageDir = "",
        tracks = "",
        tracksCount = 0
    )
}
