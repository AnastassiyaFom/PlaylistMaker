package com.practicum.playlistmaker


import java.io.Serializable

data class Track(
    val trackId:Int,
    val trackName: String,
    val artistName: String,
    var trackTime: String,
    val trackTimeMillis:Long,
    val artworkUrl100: String
): Serializable