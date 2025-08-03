package com.practicum.playlistmaker.domain.interfaces.repositories

import android.content.Context
import com.practicum.playlistmaker.domain.models.Track

interface LastCheckedTrackRepository {
    val context: Context
    fun getLastCheckedTrack(): Track?
    fun saveLastCheckedTrack(track:Track)
}