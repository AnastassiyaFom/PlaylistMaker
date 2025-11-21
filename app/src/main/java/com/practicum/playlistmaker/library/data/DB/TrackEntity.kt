package com.practicum.playlistmaker.library.data.DB

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
@Entity(tableName = "track_table")
data class TrackEntity(
@PrimaryKey
val trackId:Int,
val trackName: String,
val artistName: String,
var trackTime: String,
val artworkUrl100: String,
val artworkUrl500: String,
val collectionName:String,
val releaseDate:String,
val primaryGenreName:String,
val country:String,
val previewUrl:String,
val dateAdded: Long = Date().time
)
