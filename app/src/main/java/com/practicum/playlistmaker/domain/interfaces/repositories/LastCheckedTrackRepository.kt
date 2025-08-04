package com.practicum.playlistmaker.domain.interfaces.repositories

import android.content.Context
import com.practicum.playlistmaker.domain.models.Track

interface LastCheckedTrackRepository {

    fun getLastCheckedTrack(): Track?
    fun saveLastCheckedTrack(track:Track)
}