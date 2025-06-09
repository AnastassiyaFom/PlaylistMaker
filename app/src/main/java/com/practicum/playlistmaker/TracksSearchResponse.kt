package com.practicum.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale

class TracksSearchResponse(
    val resultCount:Int,
    val results: ArrayList<Track>) {
    fun setFormatTarckTime() {
        for (track in this.results){
            track.trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        }

    }
}
