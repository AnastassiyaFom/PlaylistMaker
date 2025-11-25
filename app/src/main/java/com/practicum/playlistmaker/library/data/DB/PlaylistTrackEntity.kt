package com.practicum.playlistmaker.library.data.DB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_track_re_table")
data class PlaylistTrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val playlistId:Int,
    val trackId:Int
)
