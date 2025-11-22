package com.practicum.playlistmaker.library.data.DB

import androidx.room.Entity

@Entity(tableName = "playlist_track_re_table")
data class PlaylistTrackEntity(
    val playlistId:Int,
    val trackId:Int
)
