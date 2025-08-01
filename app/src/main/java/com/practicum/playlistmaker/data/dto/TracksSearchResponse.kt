package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksSearchResponse(
    val resultCount:Int,
    val results: ArrayList<TrackDto>) : Response()
